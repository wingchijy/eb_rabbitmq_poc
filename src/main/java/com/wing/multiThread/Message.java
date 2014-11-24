package com.wing.multiThread;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Message
{
    private byte[] value;

	public Message(int size) throws Exception
    {
        this.value = new byte[size];

        URL url = new URL("http://www.baidu.com");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = (InputStream) conn.getContent();

        if (conn.getResponseCode() == 200){
            is.read(this.value, 0, size);
        }

        is.close();
	}

    public byte[] getValue() {
//       System.out.println( String.format("msg:%s", new String(this.value) ));
        return this.value;
    }

}

