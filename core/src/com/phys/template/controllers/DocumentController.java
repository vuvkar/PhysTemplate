package com.phys.template.controllers;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.phys.template.PhysTemplate;
import com.phys.template.models.*;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

public class DocumentController {

    private final static int INITIAL_COLUMNS = 6;

    private final static int ORDER_COL_INDEX = 0;
    private final static int RANK_INDEX = 1;
    private final static int FULL_NAME_INDEX = 2;
    private final static int AGE_GROUP_INDEX = 3;
    private final static int CATEGORY_INDEX = 4;
    private final static int RESTRICTIONS_INDEX = 5;

    private final static int HEADER_HEIGHT = 1600;
    private final static int ORDER_WIDTH = 500;
    private final static int RANK_WIDTH = 800;
    private final static int NAME_WIDTH = 5000;
    private final static int AGE_GROUP_WIDTH = 500;
    private final static int CATEGORY_WIDTH = 500;
    private final static int RESTRICTION_WIDTH = 500;
    private final static int RAW_VALUE_WIDTH = 500;
    private final static int POINT_WIDTH = 500;
    private final static int OVERALL_POINTS_WIDTH = 1300;
    private final static int GRADE_WIDTH = 1300;

    public void createDocumentFor(Project project, Path path) throws Exception {

    }

    private void configurePageSizeAndOrientation(XWPFDocument document) {
        CTBody body = document.getDocument().getBody();

        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();

        if (!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        CTPageSz pageSize = section.getPgSz();
        pageSize.setH(BigInteger.valueOf(11900));
        pageSize.setW(BigInteger.valueOf(16840));

        pageSize.setOrient(STPageOrientation.LANDSCAPE);
    }

    public void createTableStructure(XWPFTable table, Project project) {
        int exerciseSize = project.getExercises().size;

        XWPFTableRow firstRow = table.getRow(0);
        XWPFTableRow secondRow = table.createRow();

        int overallColumnSize = INITIAL_COLUMNS + 2 + exerciseSize * 2;

        // initial cell is already created
        for (int i = 1; i < overallColumnSize; i++) {
            firstRow.addNewTableCell();
            secondRow.addNewTableCell();
        }

        int peopleCount = project.getPeopleCount();
        for (int i = 0; i < peopleCount; i++) {
            table.createRow();
        }

        for (int i = 0; i < INITIAL_COLUMNS; i++) {
            mergeCellVertically(table, i, 0, 1);
        }

        for (int i = 6; i < INITIAL_COLUMNS + exerciseSize * 2; i += 2) {
            mergeCellHorizontally(table, 0, i, i + 1);
        }

        for (int i = INITIAL_COLUMNS + exerciseSize * 2; i < overallColumnSize; i++) {
            mergeCellVertically(table, i, 0, 1);
        }
    }

    public XWPFDocument createDocumentForProject(Project project) throws Exception {
        //Blank Document
        XWPFDocument document = new XWPFDocument();
        configurePageSizeAndOrientation(document);

        //Header and date
        configureHeaderAndDate(document, project);

        //create table
        XWPFTable table = document.createTable();

        createTableStructure(table, project);

        // configure table cells size
        configureTableCellsSize(table, project);

        //fill table data
        fillTableInfo(table, project);

        //align table data
        formatTableCells(table, project);

        //add statistics
        addStatistics(document, project);
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJcTable jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
        STJcTable.Enum en = STJcTable.Enum.forInt(1);
        jc.setVal(en);

        return document;
    }

    private void addStatistics(XWPFDocument document, Project project) {
        Metadata metadata = project.getMetadata();
        document.createParagraph().createRun().addBreak(BreakType.PAGE);

        XWPFParagraph statisticsParagraph = document.createParagraph();
        XWPFRun run = statisticsParagraph.createRun();
        run.setBold(true);
        run.setFontSize(13);
        run.setFontFamily("GHEA Grapalat");
        run.setText("Անձնակազմն ստուգված է");
        run.addBreak();
        run.setText("ՀՀ ՊՆ " + metadata.getBaseName() + " զորամասի շտաբի պետ՝");
        run.addTab();
        run.setText("_____________________________________________");
        run.addBreak();
        run.setText("Կ․տ․");
        run.addBreak();
        run.addBreak();
        run.addBreak();

        XWPFRun statisticsRun = statisticsParagraph.createRun();
        statisticsRun.setFontSize(13);
        statisticsRun.setFontFamily("GHEA Grapalat");

        statisticsRun.setText("-Ցուցակով");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText(project.getPeopleCount() + " զինծառայող");
        statisticsRun.addBreak();

        statisticsRun.setText("-Ստուգված է");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText(project.getCheckedCount() + " զինծառայող");
        statisticsRun.addBreak();
        statisticsRun.addBreak();

        statisticsRun.setText("-գերազանց");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText(project.getCountForGrade(Grade.EXCELLENT) + " զինծառայող, " + project.getPercentForGrade(Grade.EXCELLENT)+"%");
        statisticsRun.addBreak();

        statisticsRun.setText("-լավ");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText(project.getCountForGrade(Grade.GOOD) + " զինծառայող, " + project.getPercentForGrade(Grade.GOOD)+"%");
        statisticsRun.addBreak();

        statisticsRun.setText("-բավարար");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText(project.getCountForGrade(Grade.OK) + " զինծառայող, " + project.getPercentForGrade(Grade.OK)+"%");
        statisticsRun.addBreak();

        statisticsRun.setText("-անբավարար");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText(project.getCountForGrade(Grade.BAD) + " զինծառայող, " + project.getPercentForGrade(Grade.BAD)+"%");
        statisticsRun.addBreak();

        statisticsRun.setText("Կատարման տոկոսը՝ ");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText(project.getNormalPercent()+"%");
        statisticsRun.addBreak();

        statisticsRun.setText("Ընդհանուր գնահատականը՝");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText(project.getFinalGrade().toString());
        statisticsRun.addBreak();
        statisticsRun.addBreak();
        statisticsRun.addBreak();

        statisticsRun.setText("Ստորաբաժանման հրամանատար՝");
        statisticsRun.addTab();
        statisticsRun.setText("_____________________________________________");
        statisticsRun.addBreak();
        statisticsRun.addBreak();
        statisticsRun.setText("Ստուգող՝");
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.addTab();
        statisticsRun.setText("_____________________________________________");
    }

    private void configureHeaderAndDate(XWPFDocument document, Project project) {
        XWPFParagraph headerParagraph = document.createParagraph();
        XWPFRun run = headerParagraph.createRun();
        run.setBold(true);
        run.setFontSize(13);
        run.setFontFamily("GHEA Grapalat");
        headerParagraph.setAlignment(ParagraphAlignment.CENTER);
        Metadata metadata = project.getMetadata();
        run.setText("ՀՀ ՊՆ " + metadata.getBaseName() + " ԶՈՐԱՄԱՍԻ " +
                metadata.getSquadName().toUpperCase() + "Ի ՖԻԶԻԿԱԿԱՆ ՊԱՏՐԱՍՏՈՒԹՅԱՆ ՍՏՈՒԳՄԱՆ ԱՐԴՅՈՒՆՔՆԵՐԻ" +
                "\n ԱՄՓՈՓԱԳԻՐ");
        run.addBreak();

        XWPFParagraph dateParagraph = document.createParagraph();
        XWPFRun dateRun = dateParagraph.createRun();
        dateRun.setFontSize(13);
        dateRun.setFontFamily("GHEA Grapalat");
        dateRun.setText("«___» ________________ 20__թ.");
        dateRun.addBreak();
    }

    private void configureTableCellsSize(XWPFTable table, Project project) {
        XWPFTableRow firstRow = table.getRow(0);
        XWPFTableRow secondRow = table.getRow(1);

        firstRow.setHeight(HEADER_HEIGHT);
        secondRow.setHeight(HEADER_HEIGHT);

        XWPFTableCell orderCell = firstRow.getCell(0);
        orderCell.setWidthType(TableWidthType.DXA);
        orderCell.setWidth(String.valueOf(ORDER_WIDTH));

        XWPFTableCell rankCell = firstRow.getCell(1);
        rankCell.setWidthType(TableWidthType.DXA);
        rankCell.setWidth(String.valueOf(RANK_WIDTH));

        XWPFTableCell nameCell = firstRow.getCell(2);
        nameCell.setWidthType(TableWidthType.DXA);
        nameCell.setWidth(String.valueOf(NAME_WIDTH));

        XWPFTableCell ageGroupCell = firstRow.getCell(3);
        ageGroupCell.setWidthType(TableWidthType.DXA);
        ageGroupCell.setWidth(String.valueOf(AGE_GROUP_WIDTH));

        XWPFTableCell categoryCell = firstRow.getCell(4);
        categoryCell.setWidthType(TableWidthType.DXA);
        categoryCell.setWidth(String.valueOf(CATEGORY_WIDTH));

        XWPFTableCell restrictionCell = firstRow.getCell(5);
        restrictionCell.setWidthType(TableWidthType.DXA);
        restrictionCell.setWidth(String.valueOf(RESTRICTION_WIDTH));

        int exerciseSize = project.getExercises().size;

        for (int i = 0; i < exerciseSize; i++) {
            XWPFTableCell cellRawValue = secondRow.getCell(INITIAL_COLUMNS + i * 2);
            cellRawValue.setWidthType(TableWidthType.DXA);
            cellRawValue.setWidth(String.valueOf(RAW_VALUE_WIDTH));

            XWPFTableCell cellPointValue = secondRow.getCell(INITIAL_COLUMNS + i * 2 + 1);
            cellPointValue.setWidthType(TableWidthType.DXA);
            cellPointValue.setWidth(String.valueOf(POINT_WIDTH));

            XWPFTableCell exerciseCell = firstRow.getCell(INITIAL_COLUMNS + i * 2);
            exerciseCell.setWidthType(TableWidthType.DXA);
            exerciseCell.setWidth(String.valueOf(POINT_WIDTH + RAW_VALUE_WIDTH));
        }

        int offset = INITIAL_COLUMNS + exerciseSize * 2;
        XWPFTableCell overallPointCell = firstRow.getCell(offset);
        overallPointCell.setWidthType(TableWidthType.DXA);
        overallPointCell.setWidth(String.valueOf(OVERALL_POINTS_WIDTH));

        XWPFTableCell gradeCell = firstRow.getCell(offset + 1);
        gradeCell.setWidthType(TableWidthType.DXA);
        gradeCell.setWidth(String.valueOf(GRADE_WIDTH));


    }

    private void formatTableCells(XWPFTable table, Project project) {
        for (int i = 0; i < project.getPeopleCount() + 2; i++) {
            XWPFTableRow row = table.getRow(i);
            int lastIndexStart = INITIAL_COLUMNS + project.getExercises().size * 2;
            for (int j = 0; j < lastIndexStart + 2; j++) {
                XWPFTableCell cell = row.getCell(j);
                XWPFParagraph xwpfParagraph = cell.getParagraphArray(0);
                xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
                List<XWPFRun> runs = xwpfParagraph.getRuns();
                if (runs.isEmpty()) {
                    continue;
                }
                runs.get(0).setFontFamily("GHEA Grapalat");
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                if (i == 0 && (j == AGE_GROUP_INDEX || j == CATEGORY_INDEX || j == RESTRICTIONS_INDEX
                        || j == lastIndexStart || j == lastIndexStart + 1)) {
                    cell.getCTTc().getTcPr().addNewTextDirection().setVal(STTextDirection.BT_LR);
                }

                if (i == 1 && (j >= INITIAL_COLUMNS && j < lastIndexStart)) {
                    cell.getCTTc().getTcPr().addNewTextDirection().setVal(STTextDirection.BT_LR);
                }
            }
        }
    }

    private void fillTableInfo(XWPFTable table, Project project) {
        XWPFTableRow firstRow = table.getRow(0);
        XWPFTableRow secondRow = table.getRow(1);

        XWPFTableCell orderCell = firstRow.getCell(ORDER_COL_INDEX);
        orderCell.setText("Հ/Հ");
        orderCell.getCTTc().addNewTcPr().addNewShd().setFill("909090");

        XWPFTableCell rankCell = firstRow.getCell(RANK_INDEX);
        rankCell.setText("Զին- \nկոչումը");
        rankCell.getCTTc().addNewTcPr().addNewShd().setFill("909090");

        XWPFTableCell nameCell = firstRow.getCell(FULL_NAME_INDEX);
        nameCell.setText("Ա․Ա․Հ․");
        nameCell.getCTTc().addNewTcPr().addNewShd().setFill("909090");


        XWPFTableCell ageGroupCell = firstRow.getCell(AGE_GROUP_INDEX);
        ageGroupCell.setText("Տարիքային խումբը");
        ageGroupCell.getCTTc().addNewTcPr().addNewShd().setFill("909090");

        XWPFTableCell categroyCell = firstRow.getCell(CATEGORY_INDEX);
        categroyCell.setText("Կատեգորիան");
        categroyCell.getCTTc().addNewTcPr().addNewShd().setFill("909090");

        XWPFTableCell restrictionsCell = firstRow.getCell(RESTRICTIONS_INDEX);
        restrictionsCell.setText("Առողջական սահմանափակում.");
        restrictionsCell.getCTTc().addNewTcPr().addNewShd().setFill("909090");

        Array<Exercise> exercises = project.getExercises();
        int exerciseSize = exercises.size;
        for (int i = 0; i < exerciseSize; i++) {
            Exercise exercise = exercises.get(i);
            XWPFTableCell cell = firstRow.getCell(INITIAL_COLUMNS + i * 2);
            cell.setText(exercise.getVeryShortDescription());
            cell.getCTTc().addNewTcPr().addNewShd().setFill("909090");

            XWPFTableCell cell1 = secondRow.getCell(INITIAL_COLUMNS + i * 2);
            cell1.setText("արդյունքը");
            cell1.getCTTc().addNewTcPr().addNewShd().setFill("909090");

            XWPFTableCell cell2 = secondRow.getCell(INITIAL_COLUMNS + i * 2 + 1);
            cell2.setText("բալը");
            cell2.getCTTc().addNewTcPr().addNewShd().setFill("909090");
        }

        XWPFTableCell overallPointCell = firstRow.getCell(INITIAL_COLUMNS + 2 * exerciseSize);
        overallPointCell.setText("Վարժությունների կատարման ընդհանուր բալը");
        overallPointCell.getCTTc().addNewTcPr().addNewShd().setFill("909090");

        XWPFTableCell gradeCell = firstRow.getCell(INITIAL_COLUMNS + 2 * exerciseSize + 1);
        gradeCell.setText("Ֆիզիկական պատրաստվածության ընդհանուր գնահատականը");
        gradeCell.getCTTc().addNewTcPr().addNewShd().setFill("909090");

        fillPeopleData(table, project);
    }

    private void fillPeopleData(XWPFTable table, Project project) {
        ArrayList<Person> people = project.getPeople();
        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);

            XWPFTableRow row = table.getRow(i + 2);
            row.getCell(ORDER_COL_INDEX).setText(String.valueOf(person.index + 1));
            row.getCell(RANK_INDEX).setText(person.rank.shortName());
            row.getCell(FULL_NAME_INDEX).setText(person.getFullName());
            row.getCell(AGE_GROUP_INDEX).setText(String.valueOf(person.ageGroup.number));
            row.getCell(CATEGORY_INDEX).setText(String.valueOf(person.category.ordinal() + 1));

            StringBuilder restrictionText = new StringBuilder();
            for (Restriction restriction : person.restrictions) {
                restrictionText.append(restriction.getName()).append(", ");
            }

            if (restrictionText.length() > 0) {
                row.getCell(RESTRICTIONS_INDEX).setText(restrictionText.substring(0, restrictionText.length() - 2));
            }

            Array<Exercise> exercises = project.getExercises();
            int exerciseSize = exercises.size;
            int j = 0;
            for (Exercise exercise : exercises) {
                int exerciseNumber = exercise.number;
                XWPFTableCell valueCell = row.getCell(INITIAL_COLUMNS + j * 2);
                XWPFTableCell pointCell = row.getCell(INITIAL_COLUMNS + j++ * 2 + 1);

                if (PhysTemplate.Instance().ProjectController().isPersonRestrictedFrom(person, exerciseNumber)) {
                    valueCell.getCTTc().addNewTcPr().addNewShd().setFill("7f00ff");
                    pointCell.getCTTc().addNewTcPr().addNewShd().setFill("7f00ff");
                    continue;
                }

                if (!person.availableExercises.contains(exerciseNumber)) {
                    valueCell.getCTTc().addNewTcPr().addNewShd().setFill("ff0000");
                    pointCell.getCTTc().addNewTcPr().addNewShd().setFill("ff0000");
                    continue;
                }

                String rawValue = "-";

                boolean hasFilled = person.hasFilledRawValue(exerciseNumber);
                if (hasFilled) {
                    if (PhysTemplate.Instance().DataController().isFloatExercise(exerciseNumber)) {
                        rawValue = String.valueOf(person.getFloatExerciseRawValue(exerciseNumber));
                    } else {
                        rawValue = String.valueOf(person.getIntExerciseRawValue(exerciseNumber));
                    }
                }
                valueCell.setText(rawValue);

                String pointValue = "-";
                if (hasFilled) {
                    pointValue = String.valueOf(person.getExercisePoint(exerciseNumber));
                }
                pointCell.setText(pointValue);
            }

            int offset = INITIAL_COLUMNS + exerciseSize * 2;
            row.getCell(offset).setText(String.valueOf(person.getOverallPoints()));

            String finalGrade = "-";
            if (person.canCalculateFinalGrade) {
                Grade grade = person.getGrade();
                finalGrade = grade.getNumericalGrade() + " " + grade.getDescription(true, true);
            }
            row.getCell(offset + 1).setText(finalGrade);
        }
    }

    private void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                vmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                vmerge.setVal(STMerge.CONTINUE);
                // and the content should be removed
                for (int i = cell.getParagraphs().size(); i > 0; i--) {
                    cell.removeParagraph(0);
                }
                cell.addParagraph();
            }
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setVMerge(vmerge);
            } else {
                // only set an new TcPr if there is not one already
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setVMerge(vmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }

    private void mergeCellHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
        for (int colIndex = fromCol; colIndex <= toCol; colIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(colIndex);
            CTHMerge hmerge = CTHMerge.Factory.newInstance();
            if (colIndex == fromCol) {
                // The first merged cell is set with RESTART merge value
                hmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                hmerge.setVal(STMerge.CONTINUE);
                // and the content should be removed
                for (int i = cell.getParagraphs().size(); i > 0; i--) {
                    cell.removeParagraph(0);
                }
                cell.addParagraph();
            }
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setHMerge(hmerge);
            } else {
                // only set an new TcPr if there is not one already
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setHMerge(hmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }
}
