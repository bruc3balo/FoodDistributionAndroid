package com.victoria.fooddistribution.utils;



public class JsonErrorResponse {
	private boolean success;
	private boolean has_error;
	private int api_code;
	private String api_code_description;

	private String trx_id;
	private Object errors;

	private Object data;

	public JsonErrorResponse() {
		super();
	}

	public JsonErrorResponse(boolean success, boolean has_error, int api_code, String api_code_description, String trx_id, Object errors) {
		super();
		this.success = success;
		this.has_error = has_error;
		this.api_code = api_code;
		this.api_code_description = api_code_description;
		this.trx_id = trx_id;
		this.errors = errors;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isHas_error() {
		return has_error;
	}

	public void setHas_error(boolean has_error) {
		this.has_error = has_error;
	}

	public int getApi_code() {
		return api_code;
	}

	public void setApi_code(int api_code) {
		this.api_code = api_code;
	}

	public String getApi_code_description() {
		return api_code_description;
	}

	public void setApi_code_description(String api_code_description) {
		this.api_code_description = api_code_description;
	}

	public String getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}

	public Object getErrors() {
		return errors;
	}

	public void setErrors(Object errors) {
		this.errors = errors;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
