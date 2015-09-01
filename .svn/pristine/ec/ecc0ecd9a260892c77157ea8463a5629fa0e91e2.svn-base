/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.support;

import com.claim.connection.ResourcesProperties;
import com.claim.constants.ConstantFtp;
import com.claim.controller.FileTransferController;
import com.claim.object.FtpProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author POOL_LAPTOP
 */
public class FtpUtil {

    public static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER STATUS CODE :: " + aReply);
                Console.LOG("SERVER STATUS CODE :: " + aReply, 1);
            }
        }
    }

    public static void ftpCreateDirectoryTree(FTPClient client, String dirTree) throws IOException {

        boolean dirExists = true;

        //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
        String[] directories = dirTree.split("/");
        for (String dir : directories) {
            if (!dir.isEmpty()) {
                if (dirExists) {
                    dirExists = client.changeWorkingDirectory(dir);
                }
                if (!dirExists) {
                    if (!client.makeDirectory(dir)) {
                        throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + client.getReplyString() + "'");
                    }
                    if (!client.changeWorkingDirectory(dir)) {
                        throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + client.getReplyString() + "'");
                    }
                }
            }
        }
    }

    /*
     http://www.codejava.net/java-se/networking/ftp/java-ftp-file-upload-tutorial-and-example
     */
    public void uploadSingleFileWithFTP(String direcPath, FileTransferController fileTransferController) {
        FTPClient ftpClient = new FTPClient();
        InputStream inputStream = null;
        try {
            FtpProperties properties = new ResourcesProperties().loadFTPProperties();
            ftpClient.connect(properties.getFtp_server(), properties.getFtp_port());
            ftpClient.login(properties.getFtp_username(), properties.getFtp_password());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            File secondLocalFile = new File(direcPath);
            String secondRemoteFile = "WorkshopDay2.docx";
            inputStream = new FileInputStream(secondLocalFile);
            System.out.println("Start uploading second file");
            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            inputStream.close();
            outputStream.close();
            boolean completed = ftpClient.completePendingCommand();
            if (completed) {
                System.out.println("The second file is uploaded successfully.");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void uploadMutiFileWithFTP(String targetDirectory, FileTransferController fileTransferController) {
        try {
            FtpProperties properties = new ResourcesProperties().loadFTPProperties();
            
            FTPClient ftp = new FTPClient();
            ftp.connect(properties.getFtp_server());
            if (!ftp.login(properties.getFtp_username(), properties.getFtp_password())) {
                ftp.logout();
            }
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
            ftp.enterLocalPassiveMode();
            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.changeWorkingDirectory(targetDirectory);
            System.out.println("Current directory is " + ftp.printWorkingDirectory());
            FTPFile[] ftpFiles = ftp.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                for (FTPFile file : ftpFiles) {
                    if (!file.isFile()) {
                        continue;
                    }
                    System.out.println("File is " + file.getName());
                    OutputStream output;
                    output = new FileOutputStream(properties.getFtp_remote_directory() + File.separator + file.getName());
                    ftp.retrieveFile(file.getName(), output);
                    output.close();
                }
            }
            ftp.logout();
            ftp.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
