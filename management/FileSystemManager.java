package management;

import management.exceptions.InvalidEntryException;
import management.exceptions.InvalidSNodeException;
import management.exceptions.VirtualFileNotFoundException;
import model.FileType;

public class FileSystemManager implements VirtualDiskInspectionInterface, FileManagementInterface {

	@Override
	public boolean addDirectory(String pathname, String filename)
			throws InvalidEntryException, VirtualFileNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addFile(String pathname, String filename, FileType type, int length)
			throws InvalidEntryException, VirtualFileNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFile(String pathname, String filename)
			throws InvalidEntryException, VirtualFileNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] listDirectory(String pathname) throws InvalidEntryException, VirtualFileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean parseCommandFile(String pathname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveVirtualDisk() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSNodeInfo(int snodeId) throws InvalidSNodeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSnodeBitmap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataBlockBitmap() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
