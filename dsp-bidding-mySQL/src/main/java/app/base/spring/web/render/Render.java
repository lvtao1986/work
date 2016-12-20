package app.base.spring.web.render;

import javax.servlet.http.HttpServletResponse;

public interface Render {

	/**
	 * 设置结果数据编码
	 * 
	 * @param encoding
	 */
	void setEncoding(String encoding);

	/**
	 * 设置返回数据的 HTTP ContentType
	 * 
	 * @return
	 */
	String getContentType();

	/**
	 * 根据对象构造要输出的字符串
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	String getString(Object data) throws Exception;

	/**
	 * 向客户端输出字符串
	 * 
	 * @param response
	 * @param msg
	 * @throws Exception
	 */
	void output(HttpServletResponse response, String msg)
			throws Exception;

}
