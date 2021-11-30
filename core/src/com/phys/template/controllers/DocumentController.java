package com.phys.template.controllers;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.phys.template.models.Project;
import org.apache.poi.POIDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Path;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

public class DocumentController {

    public void createDocumentFor (Project project, Path path) throws Exception {

    }

    private void configurePageSizeAndOrientation(XWPFDocument document) {
        CTBody body = document.getDocument().getBody();

        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();

        if(!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        CTPageSz pageSize = section.getPgSz();
        pageSize.setH(BigInteger.valueOf(11900));
        pageSize.setW(BigInteger.valueOf(16840));

        pageSize.setOrient(STPageOrientation.LANDSCAPE);
    }

    public void createFirstRow(XWPFTable table, Project project) {

    }

    public void createDocumentForCurrentProject() throws Exception {
        //Blank Document
        XWPFDocument document = new XWPFDocument();
        configurePageSizeAndOrientation(document);

        //create table
        XWPFTable table = document.createTable();

        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("col one, row one");
        tableRowOne.addNewTableCell().setText("col two, row one");
        tableRowOne.addNewTableCell().setText("col three, row one");

        //create second row
        XWPFTableRow tableRowTwo = table.createRow();
        tableRowTwo.getCell(0).setText("col one, row two");
        tableRowTwo.getCell(1).setText("col two, row two");
        tableRowTwo.getCell(2).setText("col three, row two");

        //create third row
        XWPFTableRow tableRowThree = table.createRow();
        tableRowThree.getCell(0).setText("col one, row three");
        tableRowThree.getCell(1).setText("col two, row three");
        tableRowThree.getCell(2).setText("col three, row three");

        //Write the Document in file system
        FileHandle fileHandle = Gdx.files.getFileHandle("C:\\Users\\vuvka\\Desktop\\a.docx", Files.FileType.Absolute);
        FileOutputStream out = new FileOutputStream(fileHandle.path());

        document.write(out);
        out.close();
        System.out.println("create_table.docx written successully");
    }
}
