/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.connection.DBManage;
import com.claim.connection.ResourcesProperties;
import com.claim.constants.ConstantFtp;
import com.claim.dao.FileTransferDao;
import com.claim.object.FtpProperties;
import com.claim.object.ObjFileTransfer;
import com.claim.support.Console;
import com.claim.support.FtpUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

/**
 *
 * @author POOL_LAPTOP SFTP in localhost ::
 * http://www.vsysad.com/2013/06/install-and-configure-ftp-over-ssl-ftps-in-iis-7-5/
 */
public class FileTransferController {

    static Properties props;

    public static void main(String[] args) {
    }

    public void readFilesFromServer(String targetDirectory) {
        FTPClient ftpClient = new FTPClient();
        try {

            FtpProperties properties = new ResourcesProperties().loadFTPProperties();

            ftpClient.connect(properties.getFtp_server(), properties.getFtp_port());
            ftpClient.login(properties.getFtp_username(), properties.getFtp_password());
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            //FTP_REMOTE_HOME = ftpClient.printWorkingDirectory();
            FTPFile[] ftpFiles = ftpClient.listFiles();

            if (ftpFiles != null && ftpFiles.length > 0) {
                //loop thru files
                for (FTPFile file : ftpFiles) {
                    if (!file.isFile()) {
                        continue;
                    }
                    System.out.println("File is " + file.getName());

                    //get output stream
                    OutputStream output;
                    //output = new FileOutputStream(FTP_REMOTE_HOME + "/" + file.getName());
                    output = new FileOutputStream(file.getName());
                    //get the file from the remote system
                    ftpClient.retrieveFile(file.getName(), output);
                    //close output stream
                    output.close();

                    //delete the file
                    //ftpClient.deleteFile(file.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient != null) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileTransferController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<ObjFileTransfer> getFileTransfer(String reportType) {
        Connection connection = null;
        List<ObjFileTransfer> listFtps = new ArrayList<>();
        try {
            connection = new DBManage().open();
            FileTransferDao ftpDao = new FileTransferDao();
            ftpDao.setConnection(connection);

            listFtps = ftpDao.readFileList(reportType);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(InitProgramController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return listFtps;
    }

    public boolean uploadMutiFilesWithFTP(ObjFileTransfer ftpObj) throws Exception {
        FTPClient ftpClient = new FTPClient();
        int replyCode;
        boolean completed = false;
        try {

            FtpProperties properties = new ResourcesProperties().loadFTPProperties();

            try {
                ftpClient.connect(properties.getFtp_server(), properties.getFtp_port());
                FtpUtil.showServerReply(ftpClient);
            } catch (ConnectException ex) {
                FtpUtil.showServerReply(ftpClient);
                Console.LOG(ex.getMessage(), 0);
                System.out.println("ConnectException: " + ex.getMessage());
                ex.printStackTrace();
            } catch (SocketException ex) {
                FtpUtil.showServerReply(ftpClient);
                Console.LOG(ex.getMessage(), 0);
                System.out.println("SocketException: " + ex.getMessage());
                ex.printStackTrace();
            } catch (UnknownHostException ex) {
                FtpUtil.showServerReply(ftpClient);
                Console.LOG(ex.getMessage(), 0);
                System.out.println("UnknownHostException: " + ex.getMessage());
                ex.printStackTrace();
            }

            replyCode = ftpClient.getReplyCode();
            FtpUtil.showServerReply(ftpClient);

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                FtpUtil.showServerReply(ftpClient);
                ftpClient.disconnect();
                Console.LOG("Exception in connecting to FTP Serve ", 0);
                throw new Exception("Exception in connecting to FTP Server");
            } else {
                FtpUtil.showServerReply(ftpClient);
                Console.LOG("Success in connecting to FTP Serve ", 1);
            }

            try {
                boolean success = ftpClient.login(properties.getFtp_username(), properties.getFtp_password());
                FtpUtil.showServerReply(ftpClient);
                if (!success) {
                    throw new Exception("Could not login to the FTP server.");
                } else {
                    Console.LOG("login to the FTP server. Successfully ", 1);
                }
                //ftpClient.enterLocalPassiveMode();
            } catch (FTPConnectionClosedException ex) {
                FtpUtil.showServerReply(ftpClient);
                Console.LOG(ex.getMessage(), 0);
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //FTP_REMOTE_HOME = ftpClient.printWorkingDirectory();

            // APPROACH #2: uploads second file using an OutputStream
            File files = new File(ftpObj.getFtp_directory_path());

            String workingDirectoryReportType = properties.getFtp_remote_directory() + File.separator + ftpObj.getFtp_report_type();
            FtpUtil.ftpCreateDirectoryTree(ftpClient, workingDirectoryReportType);
            FtpUtil.showServerReply(ftpClient);

            String workingDirectoryStmp = workingDirectoryReportType + File.separator + ftpObj.getFtp_stmp();
            FtpUtil.ftpCreateDirectoryTree(ftpClient, workingDirectoryStmp);
            FtpUtil.showServerReply(ftpClient);

            for (File file : files.listFiles()) {
                if (file.isFile()) {
                    System.out.println("file ::" + file.getName());
                    InputStream in = new FileInputStream(file);
                    ftpClient.changeWorkingDirectory(workingDirectoryStmp);
                    completed = ftpClient.storeFile(file.getName(), in);
                    in.close();
                    Console.LOG(" อัพโหลดไฟล์ " + file.getName() + " เรียบร้อย", 1);
                    FtpUtil.showServerReply(ftpClient);
                }
            }
            Console.LOG(" กำลังตรวจสอบ... ", 1);

            //completed = ftpClient.completePendingCommand();
            FtpUtil.showServerReply(ftpClient);
            completed = true;
            ftpClient.disconnect();

        } catch (IOException ex) {
            Console.LOG(ex.getMessage(), 0);
            FtpUtil.showServerReply(ftpClient);
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                FtpUtil.showServerReply(ftpClient);
                Console.LOG(ex.getMessage(), 0);
                ex.printStackTrace();
            }
        }
        return completed;
    }

    public boolean uploadMutiFilesWithFTPS(ObjFileTransfer ftpObj) throws Exception {
        FTPSClient ftpsClient = null;
        int replyCode;
        boolean completed = false;
        try {

            FtpProperties properties = new ResourcesProperties().loadFTPProperties();

            try {
                ftpsClient = new FTPSClient(properties.getFtp_protocal(), properties.isFtp_impicit());
                //ftpsClient.setAuthValue(ConstantFtp.FTPS_PROTOCAL);
                ftpsClient.setDataTimeout(ConstantFtp.FTP_TIMEOUT);

                ftpsClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                System.out.print(ftpsClient.getReplyString());

                ftpsClient.connect(properties.getFtp_server(), properties.getFtp_port());
                FtpUtil.showServerReply(ftpsClient);

            } catch (ConnectException ex) {
                FtpUtil.showServerReply(ftpsClient);
                Console.LOG(ex.getMessage(), 0);
                System.out.println("ConnectException: " + ex.getMessage());
                ex.printStackTrace();
            } catch (SocketException ex) {
                FtpUtil.showServerReply(ftpsClient);
                Console.LOG(ex.getMessage(), 0);
                System.out.println("SocketException: " + ex.getMessage());
                ex.printStackTrace();
            } catch (UnknownHostException ex) {
                FtpUtil.showServerReply(ftpsClient);
                Console.LOG(ex.getMessage(), 0);
                System.out.println("UnknownHostException: " + ex.getMessage());
                ex.printStackTrace();
            }

            replyCode = ftpsClient.getReplyCode();
            FtpUtil.showServerReply(ftpsClient);

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpsClient.disconnect();
                Console.LOG("Exception in connecting to FTP Serve ", 0);
                throw new Exception("Exception in connecting to FTP Server");
            } else {
                Console.LOG("Success in connecting to FTP Serve ", 1);
            }

            try {
                boolean success = ftpsClient.login(properties.getFtp_username(), properties.getFtp_password());

                FtpUtil.showServerReply(ftpsClient);
                if (!success) {
                    throw new Exception("Could not login to the FTP server.");
                } else {
                    Console.LOG("login to the FTP server. Successfully ", 1);
                }
                //ftpClient.enterLocalPassiveMode();
            } catch (FTPConnectionClosedException ex) {
                Console.LOG(ex.getMessage(), 0);
                FtpUtil.showServerReply(ftpsClient);
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }

            ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpsClient.execPBSZ(0);

            //FTP_REMOTE_HOME = ftpClient.printWorkingDirectory();
            String workingDirectoryReportType = properties.getFtp_remote_directory() + File.separator + ftpObj.getFtp_report_type();
            FtpUtil.ftpCreateDirectoryTree(ftpsClient, workingDirectoryReportType);
            FtpUtil.showServerReply(ftpsClient);

            String workingDirectoryStmp = workingDirectoryReportType + File.separator + ftpObj.getFtp_stmp();
            FtpUtil.ftpCreateDirectoryTree(ftpsClient, workingDirectoryStmp);
            FtpUtil.showServerReply(ftpsClient);

            // APPROACH #2: uploads second file using an OutputStream
            File files = new File(ftpObj.getFtp_directory_path());

            for (File file : files.listFiles()) {
                if (file.isFile()) {
                    System.out.println("file ::" + file.getName());
                    InputStream in = new FileInputStream(file);
                    ftpsClient.changeWorkingDirectory(workingDirectoryStmp);
                    completed = ftpsClient.storeFile(file.getName(), in);
                    in.close();
                    Console.LOG(" อัพโหลดไฟล์ " + file.getName() + " เรียบร้อย", 1);
                    FtpUtil.showServerReply(ftpsClient);
                }
            }
            Console.LOG(" กำลังตรวจสอบ... ", 1);

            //completed = ftpClient.completePendingCommand();
            FtpUtil.showServerReply(ftpsClient);
            completed = true;
            ftpsClient.disconnect();

        } catch (IOException ex) {
            Console.LOG(ex.getMessage(), 0);
            FtpUtil.showServerReply(ftpsClient);
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpsClient.isConnected()) {
                    ftpsClient.logout();
                    ftpsClient.disconnect();
                }
            } catch (IOException ex) {
                FtpUtil.showServerReply(ftpsClient);
                Console.LOG(ex.getMessage(), 0);
                ex.printStackTrace();
            }
        }
        return completed;
    }

    
    /*
    http://kodehelp.com/java-program-for-downloading-file-from-sftp-server/
    */
    public boolean uploadMutiFilesWithSFTP(ObjFileTransfer ftpObj) throws JSchException {
        boolean completed = false;
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        FtpProperties properties = null;
        try {

            properties = new ResourcesProperties().loadFTPProperties();
            JSch jsch = new JSch();
            session = jsch.getSession(properties.getFtp_username(), properties.getFtp_server(), properties.getFtp_port());
            if (session != null && session.isConnected()) {
                Console.LOG(" Contact SFTP server Successfully ", 1);
            } else {
                Console.LOG(" Contact SFTP server Fail ", 0);
                throw new Exception(" Contact SFTP server Fail ");
            }

            session.setPassword(properties.getFtp_password());
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            if (channel != null && channel.isConnected()) {
                channel.connect();
                channelSftp = (ChannelSftp) channel;
                channelSftp.cd(properties.getFtp_remote_directory());

                Console.LOG("The correct password", 1);
            } else {
                Console.LOG("Incorrect password", 0);
                throw new Exception(" Incorrect password  ");
            }

            File files = new File(ftpObj.getFtp_directory_path());

            for (File file : files.listFiles()) {
                if (file.isFile()) {
                    System.out.println("file ::" + file.getName());
                    InputStream in = new FileInputStream(file);
                    channelSftp.put(new FileInputStream(file), file.getName());
                    in.close();
                    Console.LOG(" อัพโหลดไฟล์ " + file.getName() + " เรียบร้อย", 1);
                }
            }
            completed = true;
        } catch (JSchException e) {
            Console.LOG(e.getMessage(), 0);
            e.printStackTrace();
        } catch (Exception e) {
            Console.LOG(e.getMessage(), 0);
            e.printStackTrace();
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
        }
        return completed;
    }

    public int createLogFileTransfer(ObjFileTransfer ftpObj) {
        Connection connection = null;
        int exec = 0;
        try {
            connection = new DBManage().open();
            FileTransferDao ftpDao = new FileTransferDao();
            ftpDao.setConnection(connection);

            exec = ftpDao.createLog(ftpObj);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(InitProgramController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return exec;
    }

}
