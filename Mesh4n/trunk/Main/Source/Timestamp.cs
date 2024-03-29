using System;
using System.Collections.Generic;
using System.Text;
using System.Globalization;

namespace Mesh4n
{
	/// <summary>
	/// Parses and renders <see cref="DateTime"/> instances in a format 
	/// compliant with RFC 3339 (see http://www.ietf.org/rfc/rfc3339.txt).
	/// </summary>
	public static class Timestamp
	{ 
		const string Rfc3339 = "yyyy'-'MM'-'dd'T'HH':'mm':'ss%K";

		public static DateTime Parse(string timestamp)
		{
			return DateTime.ParseExact(timestamp, Rfc3339, CultureInfo.CurrentCulture);
		}

		public static string ToString(DateTime timestamp)
		{
			return timestamp.ToString(Rfc3339);
		}

		public static DateTime Normalize(DateTime dateTime)
		{
			return Parse(ToString(dateTime));
		}
	}
}
