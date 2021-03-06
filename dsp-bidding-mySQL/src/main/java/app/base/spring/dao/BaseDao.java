package app.base.spring.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;

import app.common.util.LogUtil;
import app.common.util.Logs;

public class BaseDao {

	protected Logs log = LogUtil.getLog(getClass());
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcOperations jdbcTemplate;
	
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcOperations simpleJdbcTemplate;
	
	@Autowired
	@Qualifier("readJdbcTemplate")
	private JdbcOperations readJdbcTemplate;
	
	@Autowired
	@Qualifier("readSimpleJdbcTemplate")
	private SimpleJdbcOperations readSimpleJdbcTemplate;
	
	@Autowired
	@Qualifier("costJdbcTemplate")
	private JdbcOperations costJdbcTemplate;
	
	@Autowired
	@Qualifier("costSimpleJdbcTemplate")
	private SimpleJdbcOperations costSimpleJdbcTemplate;


	/**
	 * @see JdbcTemplate
	 * @see org.springframework.jdbc.core.JdbcOperations
	 * 从 Spring 3.1 JdbcTemplate 和 NamedParameterJdbcTemplate 包含了 SimpleJdbcTemplate 的所有功能，但还不完善，不能完全替代
	 */
	public SimpleJdbcOperations getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	/**
	 * @see JdbcTemplate
	 * @see org.springframework.jdbc.core.JdbcOperations
	 * 从 Spring 3.1 JdbcTemplate 和 NamedParameterJdbcTemplate 包含了 SimpleJdbcTemplate 的所有功能，但还不完善，不能完全替代
	 */
	public void setSimpleJdbcTemplate(SimpleJdbcOperations simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
		log.info(" simpleJdbcOperations injected.");
	}

	public JdbcOperations getCostJdbcTemplate() {
		return costJdbcTemplate;
	}

	public void setCostJdbcTemplate(JdbcOperations costJdbcTemplate) {
		this.costJdbcTemplate = costJdbcTemplate;
	}

	public SimpleJdbcOperations getCostSimpleJdbcTemplate() {
		return costSimpleJdbcTemplate;
	}

	public void setCostSimpleJdbcTemplate(
			SimpleJdbcOperations costSimpleJdbcTemplate) {
		this.costSimpleJdbcTemplate = costSimpleJdbcTemplate;
	}

	public JdbcOperations getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		log.info(" jdbcTemplate injected.");
	}

	public JdbcOperations getReadJdbcTemplate() {
		return readJdbcTemplate;
	}
	
	public void setReadJdbcTemplate(JdbcOperations readJdbcTemplate) {
		this.readJdbcTemplate = readJdbcTemplate;
		log.info(" readJdbcTemplate injected.");
	}

	public SimpleJdbcOperations getReadSimpleJdbcTemplate() {
		return readSimpleJdbcTemplate;
	}
	
	public void setReadSimpleJdbcTemplate(
			SimpleJdbcOperations readSimpleJdbcTemplate) {
		this.readSimpleJdbcTemplate = readSimpleJdbcTemplate;
		log.info(" readSimpleJdbcTemplate injected.");
	}

	/**
	 * 获取Oracle序列值
	 * 
	 * @param seqName
	 *            序列表名
	 * @return 序列值
	 */
	protected Long getId(String seqName) {
		String sql = "select  " + seqName + ".nextval from dual ";
		return getJdbcTemplate().queryForLong(sql);
	}

}
