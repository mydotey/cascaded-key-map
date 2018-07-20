using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;

namespace MyDotey.NameCache
{
    public class CachedName
    {
        public string Separator { get; private set; }
        public object[] NameParts { get; private set; }
        public string Name { get; private set; }

        public CachedName(string separator, params object[] nameParts)
        {
            Separator = separator;
            NameParts = nameParts;
            GenerateName();
        }

        protected virtual void GenerateName()
        {
            StringBuilder stringBuilder = new StringBuilder();
            foreach (string part in NameParts)
            {
                if (stringBuilder.Length != 0)
                    stringBuilder.Append(Separator);

                stringBuilder.Append(part);
            }

            Name = stringBuilder.ToString();
        }

        public override string ToString()
        {
            return Name;
        }

        public override bool Equals(object obj)
        {
            if (obj == null || obj.GetType() != typeof(CachedName))
                return false;

            return Name.Equals(((CachedName)obj).Name);
        }

        public override int GetHashCode()
        {
            return Name.GetHashCode();
        }

    }
}
