using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.Xml;
using System.Xml.XPath;
using System.Globalization;
using System.IO;

namespace Mesh4n
{
	public class RssFeedReader : FeedReader
	{
		public RssFeedReader(XmlReader reader)
			: base(reader)
		{
		}

		protected override XmlQualifiedName ItemName
		{
			get { return new XmlQualifiedName("item"); }
		}

		protected override Feed ReadFeed(XmlReader reader)
		{
			string title = null;
			string link = null;
			string description = null;

			MemoryStream mem = new MemoryStream();
			XmlWriter writer = XmlWriter.Create(mem);
			writer.WriteStartElement("payload");

			if (reader.ReadState == ReadState.Initial) reader.MoveToContent();

			// Check root, then move, then check again for channel.
			//if (reader.LocalName == "rss")
			//{
				if (reader.Read())
				{
					if (reader.MoveToContent() == XmlNodeType.Element &&
						reader.LocalName == "channel")
					{
						while (reader.Read() && !IsItemElement(reader, XmlNodeType.Element))
						{
							if (reader.NodeType == XmlNodeType.Element)
							{
								if (reader.LocalName == "title" && reader.NamespaceURI.Length == 0)
								{
									title = ReadElementValue(reader);
								}
								else if (reader.LocalName == "link" && reader.NamespaceURI.Length == 0)
								{
									link = ReadElementValue(reader);
								}
								else if (reader.LocalName == "description" && reader.NamespaceURI.Length == 0)
								{
									description = ReadElementValue(reader);
								}
								else
								{
									writer.WriteNode(reader.ReadSubtree(), false);
								}
							}
						}

						writer.WriteEndElement();
						writer.Close();
						mem.Position = 0;

						XmlDocument doc = new XmlDocument();
						doc.Load(mem);
						XmlElement payload = doc.DocumentElement;

						return new Feed(title, link, description, payload);
					}
				}
			//}

			return null;
		}

		protected override IXmlItem ReadItem(XmlReader reader)
		{
			if (reader.ReadState == ReadState.Initial)
				reader.MoveToContent();
			if (!IsItemElement(reader, XmlNodeType.Element))
				throw new InvalidOperationException();

			string title = null;
			string description = null;
			DateTime lastUpdated = DateTime.MinValue;

			MemoryStream mem = new MemoryStream();
			XmlWriter writer = XmlWriter.Create(mem);
			writer.WriteStartElement("payload");

			while (reader.Read())
			{
				if (reader.NodeType == XmlNodeType.Element)
				{
					// We persist all elements (except title & description) in the payload, as they may 
					// be needed for further processing by other libraries.
					if (reader.LocalName == "title" && reader.NamespaceURI.Length == 0)
					{
						title = ReadElementValue(reader);
					}
					else if (reader.LocalName == "pubDate" && reader.NamespaceURI.Length == 0)
					{
						string value = ReadElementValue(reader);
						lastUpdated = RssDateTime.Parse(value).LocalTime;
						writer.WriteElementString("pubDate", value);
					}
					else if (reader.LocalName == "description" && reader.NamespaceURI.Length == 0)
					{
						description = ReadElementValue(reader);
					}
					else if (reader.LocalName == "author" && reader.NamespaceURI.Length == 0)
					{
						// author is autogenerated on write, so we skip it in the input.
						// TODO: check if assumption is OK.
						ReadElementValue(reader);
					}
					// Anything that is unknown to us is payload.
					else
					{
						writer.WriteNode(reader.ReadSubtree(), false);
					}
				}
				else if (IsItemElement(reader, XmlNodeType.EndElement))
				{
					break;
				}
			}

			writer.WriteEndElement();
			writer.Close();
			mem.Position = 0;

			XmlDocument doc = new XmlDocument();
			doc.Load(mem);
			XmlElement payload = doc.DocumentElement;

			return new XmlItem(title, description, payload, lastUpdated);
		}
	}
}
