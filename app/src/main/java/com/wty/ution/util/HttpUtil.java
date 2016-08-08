package com.wty.ution.util;

import android.annotation.SuppressLint;
import android.util.Log;


import com.wty.ution.base.AppContext;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

@SuppressLint("DefaultLocale")
public class HttpUtil {

    /**
     * 功能描述：调用post请求，不带session与用户名
     */
    public static String interactPostWithServer(String url, String args, String usernumber) throws Exception {
        return interactPostWithServer(url, args, null, usernumber, "");

    }

    /**
     * 与后台服务通讯 cj
     */
    public static String interactPostWithServer(String url, String args, UUID sessionid, String usernumber, String methodName) throws Exception {

    	url = appendSecurity(url);
        String requestid = UUID.randomUUID().toString();
        
        HttpClient hClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        
        
        httpPost.setEntity(new StringEntity(args, "UTF-8"));
        initHeader(httpPost,sessionid,usernumber,requestid);
        HttpResponse httpResponse = hClient.execute(httpPost);

        if (null != httpResponse) {
            
            if (null != httpResponse.getStatusLine()) {
            	String responentity = parseResponse(httpResponse);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {// 200为成功
                    return responentity;
                } else if (httpResponse.getStatusLine().getStatusCode() == 401) {
                    
                    // session过期了
                    //new SystemLogicHelper().runReLogin();
                    return null;
                } else if (httpResponse.getStatusLine().getStatusCode() >= 500){
                	return "连接服务器失败，请稍后再试！";
                	
                }else {
                    return responentity;
                }
            }else{

            }
            
            
        }else{

        }
        return null;
    }

    public static synchronized HttpClient getHttpClient() throws NoSuchAlgorithmException,
            CertificateException, IOException, KeyStoreException, KeyManagementException,
            UnrecoverableKeyException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        SSLSocketFactory sf = new MySSLSocketFactory(keyStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params, true);

        ConnManagerParams.setTimeout(params, 30000);
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        HttpConnectionParams.setSoTimeout(params, 30000);

        SchemeRegistry schreg = new SchemeRegistry();
        schreg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schreg.register(new Scheme("https", sf, 443));

        ClientConnectionManager conman = new ThreadSafeClientConnManager(params, schreg);
        HttpClient client = new DefaultHttpClient(conman, params);
//         HttpClient client = new DefaultHttpClient();
        return client;

    }

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new javax.net.ssl.X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[] {};
                }

            };
            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {

            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }
    }

    public static HostnameVerifier hv = new HostnameVerifier() {

    	@Override
        public boolean verify(String urlHostName,SSLSession session) {
    		return true;
    	}
    };

    public static void trustAllHttpsCertificates() throws Exception {

        // Create a trust manager that does not validate certificate chains:

        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    }

    public static class miTM implements TrustManager, javax.net.ssl.X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }
    
    public static HttpURLConnection getHttpConnection(String serviceUrl) throws Exception{
    	
		URL url = new URL(serviceUrl);
		HttpURLConnection conn = null;
		if (serviceUrl.startsWith("https")) {
			trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			conn = (HttpsURLConnection) url.openConnection();
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        return conn;
    }
    
    public static String downloadFile(String serviceUrl,String dir,String savePath,DownloadListener listener){
		// 下载网络文件
		FileOutputStream fileOutputStream = null;
		InputStream inStream = null;

		try {
			HttpURLConnection conn =getHttpConnection(serviceUrl);
			File root = new File(AppContext.PATH);
			if (!root.exists()) {
				root.mkdirs();
            }
			
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(savePath);
            if(file.exists()){
            	file.delete();
            }
            file.createNewFile();
            
            inStream = conn.getInputStream();  
            fileOutputStream = new FileOutputStream(file);  
            
            long total = conn.getContentLength();
            System.out.println("total length = " + total);
            byte[] buf = new byte[1024 * 256];
			int ch = -1;
			int count = 0;
			int preProgress = 0;
			while ((ch = inStream.read(buf)) != -1) {
				fileOutputStream.write(buf, 0, ch);
				count += ch;
				int progress = total == -1 ? -1: (int) ((count * 100) / total);
				if(progress>preProgress){
					if(listener!=null)listener.onProgress(progress);
				}
				preProgress = progress;
			} 
			
            if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
            if(total!=-1 ){
            	
            	if(count!=total){
            		File f = new File(savePath);
            		if(f.exists()){
            			f.delete();
            		}
            		return null;
            	}
            }
            return "200";
		}catch (Exception e) {
			e.printStackTrace();
			File f = new File(savePath);
    		if(f.exists()){
    			f.delete();
    		}
		}
		finally{
			if (fileOutputStream != null) {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
    
    public static String breakPointdownloadFile(String serviceUrl, long nPos,String dir,String savePath,DownloadListener listener){
		// 下载网络文件
		InputStream inStream = null;
		RandomAccessFile file = null;

		try {
			HttpURLConnection conn =getHttpConnection(serviceUrl);
			conn.setRequestProperty("RANGE", "bytes=" + nPos);
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            file = new RandomAccessFile(savePath, "rw");
            file.seek(nPos);
            
            inStream = conn.getInputStream();  
            
            long total = conn.getContentLength();
            System.out.println("total length = " + total);
            byte[] buf = new byte[1024 * 256];
			int ch = -1;
			int count = 0;
			int preProgress = 0;
			while ((ch = inStream.read(buf)) != -1) {
				file.write(buf, 0, ch);
				count += ch;
				int progress = total == -1 ? -1: (int) ((count * 100) / total);
				if(progress>preProgress){
					if(listener!=null)listener.onProgress(progress);
				}
				preProgress = progress;
			} 
			
            if(total!=-1 ){
            	
            	if(count!=total){
            		File f = new File(savePath);
            		if(f.exists()){
            			f.delete();
            		}
            		return null;
            	}
            }
            conn.disconnect();
            return "200";
		}catch (Exception e) {
			e.printStackTrace();
			File f = new File(savePath);
    		if(f.exists()){
    			f.delete();
    		}
		}
		finally{
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public interface DownloadListener{
		public void onProgress(int progress);
	}

    private static String parseResponse(HttpResponse httpResponse) throws IllegalStateException, IOException{
    	boolean isGzip = false;
    	Header header = httpResponse.getFirstHeader("Content-Encoding");
    	if(header!=null && header.getValue().equals("gzip")){
    		isGzip = true;
    		Log.i("gzip", "true");
    	}else{
    		Log.i("gzip", "false");
    	}

        String responentity = "";
        if(isGzip){
        	ByteArrayBuffer bt = new ByteArrayBuffer(4096);
        	GZIPInputStream gis = new GZIPInputStream(httpResponse.getEntity().getContent());
        	int l;
        	byte[] tmp = new byte[4096];
        	while ((l = gis.read(tmp)) != -1) {
        		bt.append(tmp, 0, l);
        	}
        	responentity = new String(bt.toByteArray(), "utf-8");
        }else{
        	responentity = EntityUtils.toString(httpResponse.getEntity());
        }
        return responentity;
    }
    
	private static String appendSecurity(String url) throws MalformedURLException{
    	
    	String api = new URL(url).getPath();
    	long ts = System.currentTimeMillis()/1000;
        int ex = 300;
        String si = MD5Util.stringToMD5(api.substring(1)+AppContext.ApiSecretKey);
        return url+String.format("?ts=%d&ex=%d&si=%s", new Object[]{ts,ex,si});
    }

    private static void initHeader(HttpMessage httpMessage, UUID sessionid, String usernumber,String requestid){
        httpMessage.setHeader("dv",CommonUtil.getDeviceUUID(AppContext.getContext()));
        httpMessage.setHeader("device","Android");
        httpMessage.setHeader("Content-Type", "application/json");
        httpMessage.setHeader("Accept", "application/json");
        httpMessage.setHeader("appid", AppContext.APPID);
        httpMessage.setHeader("Accept-Encoding","gzip, deflate");
        if (sessionid != null) {
            // 无session请求
            httpMessage.setHeader("sig", sessionid.toString());
        } else {
            httpMessage.setHeader("sig", "");
        }
        httpMessage.setHeader("userno", usernumber);
        httpMessage.setHeader("e", "" + AppContext.getInstance().getEnterpriseNumber());
        httpMessage.setHeader("s", "m");
        httpMessage.setHeader("v", "v2");
        httpMessage.setHeader("reqid", requestid);
    }

}
