using System;
using System.Collections.Generic;
using System.Text;
using System.Globalization;
using System.Diagnostics;
using CustomerLibrary.Tests.Properties;

internal static class Guard
{
	/// <summary>
	/// Checks an argument to ensure it isn't null.
	/// </summary>
	/// <param name="value">The argument value to check.</param>
	/// <param name="argumentName">The name of the argument.</param>
	public static void ArgumentNotNull(object value, string argumentName)
	{
		if (value == null)
			throw new ArgumentNullException(argumentName);
	}

	/// <summary>
	/// Checks a string argument to ensure it isn't null or empty.
	/// </summary>
	/// <param name="argumentValue">The argument value to check.</param>
	/// <param name="argumentName">The name of the argument.</param>
	public static void ArgumentNotNullOrEmptyString(string value, string argumentName)
	{
		ArgumentNotNull(value, argumentName);

		if (value.Length == 0)
			throw new ArgumentException(String.Format(
				CultureInfo.CurrentCulture,
				Resources.Arg_NullOrEmpty),
				argumentName);
	}
}