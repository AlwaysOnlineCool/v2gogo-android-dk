package com.v2gogo.project.utils.pay;

import android.text.TextUtils;

/**
 * 支付结果
 * 
 * @author houjun
 */
public class PayResult
{
	private String resultStatus;
	private String result;
	private String memo;

	public PayResult(String rawResult)
	{
		if (TextUtils.isEmpty(rawResult))
			return;

		String[] resultParams = rawResult.split(";");
		for (String resultParam : resultParams)
		{
			if (resultParam.startsWith("resultStatus"))
			{
				resultStatus = gatValue(resultParam, "resultStatus");
			}
			if (resultParam.startsWith("result"))
			{
				result = gatValue(resultParam, "result");
			}
			if (resultParam.startsWith("memo"))
			{
				memo = gatValue(resultParam, "memo");
			}
		}
	}

	@Override
	public String toString()
	{
		return "resultStatus={" + resultStatus + "};memo={" + memo + "};result={" + result + "}";
	}

	private String gatValue(String content, String key)
	{
		String prefix = key + "={";
		return content.substring(content.indexOf(prefix) + prefix.length(), content.lastIndexOf("}"));
	}

	public String getResultStatus()
	{
		return resultStatus;
	}

	public String getMemo()
	{
		return memo;
	}

	public String getResult()
	{
		return result;
	}

}
