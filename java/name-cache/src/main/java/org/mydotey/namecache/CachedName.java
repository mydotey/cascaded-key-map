package org.mydotey.namecache;

/**
 * @author koqizhao
 *
 * Jul 20, 2018
 */
public class CachedName {

    private String _separator;
    private Object[] _nameParts;
    private String _name;

    public CachedName(String separator, Object... nameParts) {
        _separator = separator;
        _nameParts = nameParts;
        generateName();
    }

    protected void generateName() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object part : _nameParts) {
            if (stringBuilder.length() != 0)
                stringBuilder.append(_separator);
            stringBuilder.append(part);
        }

        _name = stringBuilder.toString();
    }

    public String getSeparator() {
        return _separator;
    }

    public Object[] getNameParts() {
        return _nameParts;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String toString() {
        return _name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != CachedName.class)
            return false;

        return _name.equals(((CachedName) obj)._name);
    }

    @Override
    public int hashCode() {
        return _name.hashCode();
    }
}
