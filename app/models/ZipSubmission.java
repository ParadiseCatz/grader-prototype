package models;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthony on 7/6/15.
 */
public class ZipSubmission extends Submission {
    public ZipSubmission(File file) {
        super(file);
    }

    @Override
    public void unZipFile() {
        try {
            ZipFile zipFile = new ZipFile(super.getFile());
            if (zipFile.isEncrypted()) {
                return;
            }
            zipFile.extractAll(super.getFile().getParent());
        } catch (ZipException e) {
            e.printStackTrace();
        }
        mandatoryFilesOverwrite();
    }

    private void mandatoryFilesOverwrite() {
        List<String> mandatoryFileList = new ArrayList<>();
        mandatoryFileList.add("makefile");
        mandatoryFileList.add("main.cpp");
        for (String fileName : mandatoryFileList) {
            Path FROM = Paths.get("template/" + fileName);
            Path TO = Paths.get(super.getFile().getParent() + "/" + fileName);
            CopyOption[] options = new CopyOption[]{
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES
            };
            try {
                Files.copy(FROM, TO, options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
