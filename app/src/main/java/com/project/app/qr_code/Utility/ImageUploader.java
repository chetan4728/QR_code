package com.project.app.qr_code.Utility;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ImageUploader extends AsyncTask<String, String, String> {

    File imageFile = null;
    String fileName = null;

    public ImageUploader(File imageFile, String fileName){
        this.imageFile = imageFile;
        this.fileName = fileName;
    }

    @Override
    protected String doInBackground(String... params) {
        String url_str = params[0];

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag="fSnd";

        try {
            URL url = new URL(url_str);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();

            c.setRequestMethod("POST");

            c.setDoInput(true);
            c.setDoOutput(true);

            c.setRequestProperty("Connection", "Keep-Alive");
            c.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            c.connect();

            DataOutputStream dos = new DataOutputStream(c.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + this.fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            FileInputStream fin = new FileInputStream(imageFile);

            int bytesAvailable = fin.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[ ] buffer = new byte[bufferSize];

            int bytesRead = fin.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fin.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fin.read(buffer, 0,bufferSize);
            }


            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            fin.close();
            dos.flush();
            dos.close();

            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }

        return null;
    }
}
