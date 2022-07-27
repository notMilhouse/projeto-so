package model;

public class DEntry {
    private SNodeIdentifier snodeId;
    private short entryLength;
    private FileType fileType;
    private FileName name;

    class FileName {
        private short length;
        private byte[] name;
        
        public FileName(String s) {
            name = new byte[s.length()];
            for(int i = 0; i < s.length(); i++) {
                name[i] = (byte) s.charAt(i);
            }

            length = (short) s.length();
        }

        public FileName(byte[] name) {
            this.name = new byte[name.length];
            for(int i = 0; i < name.length; i++) {
                this.name[i] = name[i];
            }

            length = (short) name.length;
        }

        public String getNameString() {
            return new String(name);
        }

        public byte[] getNameByteArray() {
            return name;
        }
    }
}
