package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Person;
import com.phys.template.views.BackgroundColor;

public class PeopleListRowWidget extends Table {

    public static int NUMBER_LENGTH = 50;
    public static int RANK_LENGTH = 80;
    public static int NAME_LENGTH = 80;
    public static int SEX_LENGTH = 90;
    public static int AGE_GROUP_LENGTH = 120;
    public static int CATEGORY_LENGTH = 130;
    public static int POINTS_LENGTH = 110;
    public static int FINAL_GRADE_LENGTH = 150;


    private VisImageButton moveTop;
    private VisImageButton moveDown;
    private VisImage invalidRowImage;

    private RowCell rank;
    private RowCell name;
    private RowCell sex;
    private RowCell ageGroup;
    private RowCell category;
    private RowCell finalPoints;
    private RowCell finalGrade;

    private Person person;


    public PeopleListRowWidget() {
        this(false);
    }

    public PeopleListRowWidget(boolean isFirstRow) {
        super();
        left().top();
        defaults().padRight(2);
        if (isFirstRow) {
            rank = new RowCell(RANK_LENGTH, true);
            add(rank).growY().padLeft(32);
            rank.setText("Կոչում");
            name = new RowCell(NAME_LENGTH, true);
            add(name).grow();
            name.setText("Ա.Ա.Հ.");
            sex = new RowCell(SEX_LENGTH, true);
            add(sex).growY();
            sex.setText("Սեռ");
            ageGroup = new RowCell(AGE_GROUP_LENGTH, true);
            add(ageGroup).growY();
            ageGroup.setText("Տարիքային խումբ");
            category = new RowCell(CATEGORY_LENGTH, true);
            add(category).growY();
            category.setText("Կատեգորիա");
            finalPoints = new RowCell(POINTS_LENGTH, true);
            add(finalPoints).growY();
            finalPoints.setText("Ընդհ. միավորներ");
            finalPoints.setWrap(true);
            finalGrade = new RowCell(FINAL_GRADE_LENGTH, true);
            add(finalGrade).growY();
            finalGrade.setText("Վերջնական գնահատական");
            finalGrade.setWrap(true);
            setBackground(PhysTemplate.Instance().UIStage().getSkin().getDrawable("slider-knob"));

        } else {
            // TODO: 11/22/2021 handle validation UI, if some exercises are missing info
            Skin skin = PhysTemplate.Instance().UIStage().getSkin();
            moveTop = new VisImageButton(skin.getDrawable("select-up"));
            setBackground(skin.getDrawable("border"));
            moveTop.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (person.index != 0) {
                        PhysTemplate.Instance().ProjectController().movePersonUp(person);
                        PhysTemplate.Instance().UIStage().updatePeopleContent();
                    }

                }
            });
            moveDown = new VisImageButton(skin.getDrawable("select-down"));
            moveDown.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (person.index != PhysTemplate.Instance().ProjectController().getPeopleCount() - 1) {
                        PhysTemplate.Instance().ProjectController().movePersonDown(person);
                        PhysTemplate.Instance().UIStage().updatePeopleContent();
                    }
                }
            });
            Table buttonsTable = new Table();
            buttonsTable.add(moveTop);
            buttonsTable.row();
            buttonsTable.add(moveDown);

            add(buttonsTable);
            rank = new RowCell(RANK_LENGTH);
            add(rank).growY();
            name = new RowCell(NAME_LENGTH);
            add(name).grow();
            sex = new RowCell(SEX_LENGTH);
            add(sex).growY();
            ageGroup = new RowCell(AGE_GROUP_LENGTH);
            add(ageGroup).growY();
            category = new RowCell(CATEGORY_LENGTH);
            add(category).growY();
            finalPoints = new RowCell(POINTS_LENGTH);
            add(finalPoints).growY();
            finalGrade = new RowCell(FINAL_GRADE_LENGTH);
            add(finalGrade).growY();
            addListeners();
            setBackground(PhysTemplate.Instance().UIStage().getSkin().getDrawable("slider-knob"));

        }
    }

    private void addListeners() {
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                PhysTemplate.Instance().UIStage().showEditPersonPopup(person);
            }
        });
    }

    public void updateForPerson(Person person) {
        rank.setText(person.rank.shortName());
        name.setText(person.getFullName());
        sex.setText(person.sex.toString());
        ageGroup.setText(person.ageGroup.toString());
        category.setText(person.category.toString());
        finalPoints.setText(50);
        finalGrade.setText(5);
        this.person = person;
    }

    private static class RowCell extends VisLabel {
        int length;

        public RowCell(int cellLength) {
            this(cellLength, false);
        }

        public RowCell(int cellLength, boolean isFirstRow) {
            super();
            LabelStyle style = getStyle();
            // TODO: 11/24/2021 optimize to have only one
            LabelStyle labelStyle = new LabelStyle();
            labelStyle.background = PhysTemplate.Instance().UIStage().getSkin().getDrawable("slider-knob-disabled");
            labelStyle.font = style.font;
            labelStyle.fontColor = style.fontColor;
            setStyle(labelStyle);
            length = cellLength;
            setAlignment(Alignment.CENTER.getAlignment());
            setWidth(cellLength);
            if (isFirstRow) {
                setWrap(true);
            } else {
                setEllipsis(true);
            }
        }

        @Override
        public float getPrefWidth() {
            return length;
        }
    }

}
