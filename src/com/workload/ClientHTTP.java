package com.workload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//import org.apache.http.HttpHeaders;
/*import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.http.Header;*/
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;/*
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;*/
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

public class ClientHTTP  implements WorkerInterface {
	String URL="";
	static volatile asyncLogger LOG=asyncLogger.getInstance();
	CloseableHttpClient httpclient;
	private RequestConfig requestConfig;
	private HttpGet httpGet;
	private HttpPost httpPost;
	private String proxy_hosts;
	private int proxy_port;
	private long endRest=0;
	private long endRead=0;
	private String output="";
	private boolean IoError=false;
	private int MillisConnTimeout;
	private int MillisSocketTiemout;
	private int connectionRequestTimeout;
	private boolean PostMethos=false;
	private boolean GETMethos=false;
	private String PoolName="";
	private boolean WithProxy=false;
	private String Tags;
	private ArrayList<BasicNameValuePair> postParameters;
	
	public ClientHTTP(int MillisConnTimeout,int MillisSocketTiemout,int connectionRequestTimeout,String URL) {
		this.URL=URL;
		
		// TODO Auto-generated constructor stub
		/*
		this.proxy_hosts="cstritonwsg04.office.corp.sia.it";
		this.proxy_port=(int)80;
		*/
		this.setProxy_hosts("localhost");
		this.setProxy_port((int)8099);	
		this.URL=URL;
/*
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("localhost", 8888),
                new UsernamePasswordCredentials("peanoluca", "Pegulu@03"));
*/      
        this.httpclient=HttpClients.custom().build();
  //      		.setDefaultCredentialsProvider(credsProvider).build();
        
        this.httpGet = new HttpGet(this.URL);
        this.requestConfig=RequestConfig.custom()
            .setAuthenticationEnabled(true)
            .setConnectTimeout(MillisConnTimeout)
            .setSocketTimeout(MillisSocketTiemout)
            .setConnectionRequestTimeout(connectionRequestTimeout)
            //.setProxy(new HttpHost(this.proxy_hosts,this.proxy_port))
            .build();
        httpGet.setConfig(this.requestConfig);
	}

	public ClientHTTP init() {
		this.httpclient=HttpClients.custom().build();
		if(this.proxy_hosts==null) {
		 this.requestConfig=RequestConfig.custom()
		            .setAuthenticationEnabled(true)
		            .setConnectTimeout(MillisConnTimeout)
		            .setSocketTimeout(MillisSocketTiemout)
		            .setConnectionRequestTimeout(connectionRequestTimeout)
		            .build();
		} else {
			 this.requestConfig=RequestConfig.custom()
			            .setAuthenticationEnabled(true)
			            .setConnectTimeout(MillisConnTimeout)
			            .setSocketTimeout(MillisSocketTiemout)
			            .setConnectionRequestTimeout(connectionRequestTimeout)
			            .setProxy(new HttpHost(this.proxy_hosts,this.proxy_port))
			            .build();
		}
		if(this.GETMethos) {
			this.httpGet.setConfig(this.requestConfig);
		}
		if(this.PostMethos){
			this.httpPost.setConfig(this.requestConfig);
		}
		 return this;
	}
	@Override
	public void run() {
		long Start=System.currentTimeMillis();
		//Thread.sleep(300, 0);
	//	String ERROR="";
		this.endRead=0;
		String ERROR="";
		CloseableHttpResponse response = null;
		try {
			if(this.GETMethos) {
				response = this.httpclient.execute(this.httpGet);
			}
			if(this.PostMethos) {
				response = this.httpclient.execute(this.httpPost);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			this.IoError=true;
			ERROR="CONNECT-PROTOCOL-EXCEPTION";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(e instanceof org.apache.http.conn.ConnectTimeoutException) {
				ERROR="CONNECT-CONNTIMEOUT";
			}
			ERROR="CONNECT-GENERICERROR";
			this.IoError=true;
		}
		if(this.IoError==false) {
		this.endRest=System.currentTimeMillis(); 
		BufferedReader br=null;
		try {
			br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		
			char[] buffer=new char[4096];
			while (true) {
				if(br.read(buffer,0,buffer.length)>=0) {
				this.output+=buffer.toString();
				} else {
					
					break;
				}
			}
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			this.IoError=true; 
			ERROR="ERROR-READING";
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ERROR="ALLREADYCLOSED";
			}
		}
		endRead=System.currentTimeMillis();
		try {
			EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
			if(!this.IoError) {
				LOG.put(new logMessage(Start,this.endRest,this.endRead,Thread.currentThread().getName()+";"+this.Tags+";"+response.getStatusLine(),response.getAllHeaders(),this.output,true).setTags(this.Tags));
			} else {
				LOG.put(new logMessage(Start,this.endRest,this.endRead,Thread.currentThread().getName()+";"+this.Tags+";"+ERROR,null,this.output,false).setTags(this.Tags));
			}
		
	}

	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}
	
	/*
	 * Building Block
	 */
	public ClientHTTP(Builder builder) {
		this.URL=builder.URL;
		this.setProxy_hosts(builder.proxy_hosts);
		this.setProxy_port(builder.proxy_port);
		this.GETMethos=builder.GETMethos;
		this.PostMethos=builder.PostMethos;
		this.httpGet=builder.httpGet;
		this.httpPost=builder.httpPost;
		this.connectionRequestTimeout=builder.connectionRequestTimeout;
		this.MillisConnTimeout=builder.MillisConnTimeout;
		this.MillisSocketTiemout=builder.MillisSocketTiemout;
		this.PoolName=builder.PoolName;
		this.WithProxy=builder.WithProxy;
		this.setPostParameters(builder.postParameters);
		
	}
	public int getMillisConnTimeout() {
		return MillisConnTimeout;
	}

	public void setMillisConnTimeout(int millisConnTimeout) {
		MillisConnTimeout = millisConnTimeout;
	}
	public int getMillisSocketTiemout() {
		return MillisSocketTiemout;
	}

	public void setMillisSocketTiemout(int millisSocketTiemout) {
		MillisSocketTiemout = millisSocketTiemout;
	}
	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}
	public boolean isPostMethos() {
		return PostMethos;
	}

	public void setPostMethos(boolean postMethos) {
		PostMethos = postMethos;
	}
	public boolean isGETMethos() {
		return GETMethos;
	}

	public void setGETMethos(boolean gETMethos) {
		GETMethos = gETMethos;
	}
	public String getProxy_hosts() {
		return proxy_hosts;
	}

	public void setProxy_hosts(String proxy_hosts) {
		this.proxy_hosts = proxy_hosts;
	}
	public int getProxy_port() {
		return proxy_port;
	}

	public void setProxy_port(int proxy_port) {
		this.proxy_port = proxy_port;
	}
	/**
	 * @return the poolName
	 */
	public String getPoolName() {
		return PoolName;
	}

	/**
	 * @param poolName the poolName to set
	 */
	public void setPoolName(String poolName) {
		PoolName = poolName;
	}
	/**
	 * @return the withProxy
	 */
	public boolean isWithProxy() {
		return WithProxy;
	}

	/**
	 * @param withProxy the withProxy to set
	 */
	public void setWithProxy(boolean withProxy) {
		WithProxy = withProxy;
	}
	/**
	 * @return the tags
	 */
	public String getTags() {
		return Tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.Tags = tags;
	}
	/**
	 * @return the postParameters
	 */
	public ArrayList<BasicNameValuePair> getPostParameters() {
		return postParameters;
	}

	/**
	 * @param postParameters the postParameters to set
	 */
	@Override
	public void setPostParameters(ArrayList<BasicNameValuePair> postParameters) {
		this.postParameters = postParameters;
	}
	/*
	 * Definizione Del Bulider.
	 */
	public static class Builder {
		CloseableHttpClient httpclient;
		
		private RequestConfig requestConfig;
		private HttpGet httpGet=null;
		private HttpPost httpPost=null;
		private String proxy_hosts=null;
		private int proxy_port;
		private String URL=null;
		private int MillisConnTimeout=1000;
		private int MillisSocketTiemout=10000;
		private int connectionRequestTimeout=11000;
		private boolean PostMethos=false;
		private boolean GETMethos=false;
		private String PoolName="";
		private boolean WithProxy=false;
		private ArrayList<BasicNameValuePair> postParameters;
		
		
		public ClientHTTP build() {
			return new ClientHTTP(this);
		}
		
		public Builder setURL(String URL) {
			this.URL=URL;
			return this;
		}
		public String getURL(String URL) {
			return this.URL;
		}
		public RequestConfig getRequestConfig() {
			return requestConfig;
		}
		public Builder setRequestConfig(RequestConfig requestConfig) {
			this.requestConfig = requestConfig;
			return this;
		}
		public HttpGet getHttpGet() {
			return httpGet;
		}
		public Builder setHttpGet(HttpGet httpGet) {
			this.httpGet = httpGet;
			return this;
		}
		public HttpPost getHttpPost() {
			return httpPost;
		}
		public Builder setHttpPost(HttpPost httpPost) {
			this.httpPost = httpPost;
			return this;
		}
		public String getProxy_hosts() {
			return proxy_hosts;
		}
		public Builder setProxy_hosts(String proxy_hosts) {
			this.proxy_hosts = proxy_hosts;
			return this;
		}
		public int getProxy_port() {
			return proxy_port;
		}
		public Builder setProxy_port(int proxy_port) {
			this.proxy_port = proxy_port;
			return this;
		}

		public int getMillisConnTimeout() {
			return MillisConnTimeout;
		}
		public Builder setMillisConnTimeout(int millisConnTimeout) {
			MillisConnTimeout = millisConnTimeout;
			return this;
		}
		public int getMillisSocketTiemout() {
			return MillisSocketTiemout;
		}
		public Builder setMillisSocketTiemout(int millisSocketTiemout) {
			MillisSocketTiemout = millisSocketTiemout;
			return this;
		}
		public int getConnectionRequestTimeout() {
			return connectionRequestTimeout;
		}
		public Builder setConnectionRequestTimeout(int connectionRequestTimeout) {
			this.connectionRequestTimeout = connectionRequestTimeout;
			return this;
		}
		public boolean isPostMethos() {
			return PostMethos;
		}
		public Builder setPostMethos(boolean postMethos) {
			PostMethos = postMethos;
			this.httpPost = new HttpPost(this.URL);
			return this;
		}
		public boolean isGETMethos() {
			return GETMethos;
		}
		public Builder setGETMethos(boolean gETMethos) {
			GETMethos = gETMethos;
			this.httpGet = new HttpGet(this.URL);
			return this;
		}
		/**
		 * @return the poolName
		 */
		public String getPoolName() {
			return PoolName;
		}
		/**
		 * @param poolName the poolName to set
		 */
		public Builder setPoolName(String poolName) {
			PoolName = poolName;
			return this;
		}
		/**
		 * @return the withProxy
		 */
		public boolean isWithProxy() {
			return WithProxy;
		}
		/**
		 * @param withProxy the withProxy to set
		 */
		public Builder setWithProxy(boolean withProxy) {
			WithProxy = withProxy;
			return this;
		}

		/**
		 * @return the postParameters
		 */
		public ArrayList<BasicNameValuePair> getPostParameters() {
			return postParameters;
		}

		/**
		 * @param postParameters the postParameters to set
		 */
		public Builder setPostParameters(ArrayList<BasicNameValuePair> postParameters) {
			this.postParameters = postParameters;
			return this;
		}
	}
}
