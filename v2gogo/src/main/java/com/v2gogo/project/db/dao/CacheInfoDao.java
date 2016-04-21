package com.v2gogo.project.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.v2gogo.project.db.CacheInfo;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table CacheData.
 */
public class CacheInfoDao extends AbstractDao<CacheInfo, Long>
{

	public static final String TABLENAME = "CacheData";

	/**
	 * Properties of entity CacheInfo.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties
	{
		public final static Property Id = new Property(0, Long.class, "id", true, "_id");
		public final static Property Type = new Property(1, Integer.class, "type", false, "TYPE");
		public final static Property Response = new Property(2, String.class, "response", false, "RESPONSE");
	};

	public CacheInfoDao(DaoConfig config)
	{
		super(config);
	}

	public CacheInfoDao(DaoConfig config, DaoSession daoSession)
	{
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists)
	{
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'CacheData' (" + //
				"'_id' INTEGER PRIMARY KEY ," + // 0: id
				"'TYPE' INTEGER," + // 1: type
				"'RESPONSE' TEXT);"); // 2: response
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists)
	{
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CacheData'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, CacheInfo entity)
	{
		stmt.clearBindings();

		Long id = entity.getId();
		if (id != null)
		{
			stmt.bindLong(1, id);
		}

		Integer type = entity.getType();
		if (type != null)
		{
			stmt.bindLong(2, type);
		}

		String response = entity.getResponse();
		if (response != null)
		{
			stmt.bindString(3, response);
		}
	}

	/** @inheritdoc */
	@Override
	public Long readKey(Cursor cursor, int offset)
	{
		return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public CacheInfo readEntity(Cursor cursor, int offset)
	{
		CacheInfo entity = new CacheInfo( //
				cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
				cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // type
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // response
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, CacheInfo entity, int offset)
	{
		entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
		entity.setType(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
		entity.setResponse(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
	}

	/** @inheritdoc */
	@Override
	protected Long updateKeyAfterInsert(CacheInfo entity, long rowId)
	{
		entity.setId(rowId);
		return rowId;
	}

	/** @inheritdoc */
	@Override
	public Long getKey(CacheInfo entity)
	{
		if (entity != null)
		{
			return entity.getId();
		}
		else
		{
			return null;
		}
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable()
	{
		return true;
	}

}