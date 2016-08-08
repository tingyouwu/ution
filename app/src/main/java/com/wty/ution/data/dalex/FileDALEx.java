package com.wty.ution.data.dalex;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.DatabaseField.FieldType;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;
import com.wty.ution.util.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wty
 *	文件信息数据库对象
 */
public class FileDALEx extends SqliteBaseDALEx {

	private static final long serialVersionUID = 1L;
	
	public static final int STATE_DOWNLOAD_NOTSTART = 0;
	public static final int STATE_DOWNLOAD_DONE = 3;

	public static final String FILEID = "fileid";
	public static final String FILENAME = "filename";
	public static final String MD5 = "md5";
	public static final String CHUNKSIZE = "chunksize";
	public static final String CHUNKTOTAL = "chunktotal";
	public static final String UPLOADCHUNKS = "uploadchunks";
	public static final String CONTENTTYPE = "contenttype";
	public static final String URL = "url";
	public static final String LENGTH = "length";
	public static final String PATH = "path";
	public static final String FILETYPE = "fileType";
	public static final String DONELENGTH = "doneLength";
	public static final String DOWNLOADSTATE = "downloadstate";
	public static final String STARTTIME = "startTime";
	public static final String FINISHTIME = "finishTime";
    public static final String ETAG = "ETag";
	
	@DatabaseField(primaryKey = true,Type = FieldType.VARCHAR)
	private String fileid;			//文件uuid
	@DatabaseField(Type = FieldType.VARCHAR)
	private String filename;		//不带后缀名的文件名
	@DatabaseField(Type = FieldType.VARCHAR)
	private String md5;				//文件md5
	@DatabaseField(Type = FieldType.VARCHAR)
	private String fileType;        //文件类型
	@DatabaseField(Type = FieldType.INT)
	private int chunksize=0;			//文件每个包的大小
	@DatabaseField(Type = FieldType.INT)
	private int chunktotal=0;			//文件包总个数
	@DatabaseField(Type = FieldType.VARCHAR)
	private String uploadchunks; 		//文件已经上传的文件包个数
	@DatabaseField(Type = FieldType.VARCHAR)
	private String contenttype;		//文件类型
	@DatabaseField(Type = FieldType.VARCHAR)
	private String url;				//文件下载地址
	@DatabaseField(Type = FieldType.INT)
	private long length;				//文件长度
	@DatabaseField(Type = FieldType.VARCHAR)
	private String path;			//文件的真实路径
	@DatabaseField(Type = FieldType.INT)
	private long doneLength;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String startTime;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String finishTime;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String ETag;
	
	public static FileDALEx get() {
        return (FileDALEx) SqliteDao.getDao(FileDALEx.class);
    }
	
	/**
	 * 功能描述：保存单个文件信息对象
	 */
	public void save(final FileDALEx dalex){
		operatorWithTransaction(new OnTransactionListener() {
			@Override
			public boolean onTransaction(UtionDB db) {
				ContentValues values = dalex.tranform2Values();
	            if(isExist(FILEID, dalex.getFileid())){
	                db.update(TABLE_NAME, values, FILEID+"=?", new String[]{dalex.getFileid()});
	            }else{
	                db.save(TABLE_NAME, values);
	            }
	            return true;
			}
		});
    }

	/**
	 * 保存一系列文件信息对象
	 **/
	public void save(final List<FileDALEx> list){
		operatorWithTransaction(new OnTransactionListener() {
			@Override
			public boolean onTransaction(UtionDB db) {
				for (FileDALEx dalex : list) {
					ContentValues values = dalex.tranform2Values();
					if(isExist(FILEID, dalex.getFileid())){
						db.update(TABLE_NAME, values, FILEID+"=?", new String[]{dalex.getFileid()});
					}else{
						db.save(TABLE_NAME, values);
					}
				}
				return true;
			}
		});
	}
	
	public FileDALEx queryByFilename(String filename) {

        String sql =  "select * from "+ TABLE_NAME + "  where " + FILENAME + "=? ";
        String[] params = new String[] { filename };
        return (FileDALEx) findOne(sql, params);
	}

    public FileDALEx queryByFileInfo(String filename, String filepath) {
        final FileDALEx dalex = new FileDALEx();
        String sql =  "select * from "+ TABLE_NAME + "  where " + FILENAME + "=?  and " + PATH + "=?";
        String[] params = new String[] { filename, filepath};
        return (FileDALEx) findOne(sql, params);
    }

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public String getUploadchunks() {
		return uploadchunks;
	}

	public void setUploadchunks(String uploadchunks) {
		this.uploadchunks = uploadchunks;
	}
	
	public List<String> getUploadchunksList(){
	    List<String> result = new ArrayList<String>();
	    if(this.getUploadchunks()!=null){
	        String[] chunks = getUploadchunks().split(",");
	        result.addAll(Arrays.asList(chunks));
	    }
	    return result;
	}
	
	public void setUploadchunks(List<String> uploadchunks) {
		
		String chunks = "";
		if(uploadchunks.size()!=0){
		    for(int i=0;i<uploadchunks.size();i++){
		        chunks = chunks + uploadchunks.get(i);
		        if(i!=uploadchunks.size()-1){
		            chunks = chunks +",";    
		        }
		    }
		}
		this.uploadchunks = chunks;
		
	}
	
	public int countLiveTasks() {
		int result = 0;
	    Cursor cursor = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {
            	String sql = String.format("select count(*) from %s where %s <> %s and %s <> %s", 
            			TABLE_NAME, DOWNLOADSTATE, STATE_DOWNLOAD_DONE, DOWNLOADSTATE, STATE_DOWNLOAD_NOTSTART);
                cursor = db.find(sql, new String[] {});
                if (cursor != null && cursor.moveToNext()) {
                    result = cursor.getInt(cursor.getColumnIndex("count(*)"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
	}
	
	public List<FileDALEx> queryFilesByStates(int[] states) {
		final List<FileDALEx> result = new ArrayList<FileDALEx>();
		final String[] args = new String[states.length];
		for(int i = 0; i < args.length; i++)
			args[i] = DOWNLOADSTATE + "=" + states[i];
        String sql =  "select * from "+ TABLE_NAME + "  where " + TextUtils.join(" or ", args);
        String[] params = new String[] { };
	   return findList(sql, params);
	}
	
	public int getChunktotal() {
		return chunktotal;
	}

	public void setChunktotal(int chunktotal) {
		this.chunktotal = chunktotal;
	}

	public int getChunksize() {
		return chunksize;
	}

	public void setChunksize(int chunksize) {
		this.chunksize = chunksize;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

    public FileDALEx createFileDALEx(String fileid,String path) {
        FileDALEx fileDalex = FileDALEx.get();
        
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(fileid))return null;
        
        String[] pArray = path.split("\\.");
        String fileType = pArray[pArray.length-1];
        String fileName = fileid+"."+fileType;
        
        fileDalex.setFileType(fileType);
    	fileDalex.setContenttype(fileType);
        if(path.endsWith(".dat")){
        	fileDalex.setFileType("png");
        	fileDalex.setContenttype("png");
        }
        fileDalex.setPath(path);
        if(!isFileExists(fileDalex))return null;
        File file = getFile(fileDalex);
        // 创建文件数据库对象
        fileDalex.setFilename(fileName);
        fileDalex.setFileid(fileid);
        
        fileDalex.setLength(file.length());
        fileDalex.setMd5(FileUtils.getMd5ByFile(file));
        fileDalex.setPath(file.getPath());
        fileDalex.setChunksize(FileUtils.piece);
        fileDalex.setChunktotal(FileUtils.getFileChunkTotal(file));
        fileDalex.setDoneLength(0);
        fileDalex.save(fileDalex);
        return fileDalex;
    }
	
    public File getFile(FileDALEx dalex){
        File  file;
        if(!isFileExists(dalex)){
            return null;
        }else{
            file = new File(dalex.getPath());
        }
        
        return file;
    }

    public boolean isFileExists(FileDALEx dalex){
        File file = new File(dalex.getPath());
        return file.exists();
    }
    
    
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

	public long getDoneLength() {
		return doneLength;
	}

	public void setDoneLength(long doneLength) {
		this.doneLength = doneLength;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return finishTime;
	}

	public void setEndTime(String finishTime) {
		this.finishTime = finishTime;
	}
	
	public static String cropFileName(String filename){
		int end = filename.lastIndexOf(".");
		if(end != -1)
		  return filename.substring(0, end);
		return null;
	}

	public String getETag() {
		return ETag;
	}

	public void setETag(String ETag) {
		this.ETag = ETag;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
}
