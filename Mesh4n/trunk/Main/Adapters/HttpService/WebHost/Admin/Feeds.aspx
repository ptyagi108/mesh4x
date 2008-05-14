﻿<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Feeds.aspx.cs" Inherits="WebHost.Admin.Feeds" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Feeds</title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
		<asp:HyperLink ID="lnkRssFeed" runat="server" NavigateUrl="../Service.svc/feeds/{0}">Rss</asp:HyperLink><br />
		<asp:HyperLink ID="lnkKmlFeed" runat="server" NavigateUrl="../Service.svc/feeds/{0}?format=kml">Kml</asp:HyperLink><br />
		<asp:HyperLink ID="lnkKmlNetworkFeed" runat="server" NavigateUrl="../Service.svc/feeds/{0}?format=kmlNet">Kml Network</asp:HyperLink><br />
    </div>
    </form>
</body>
</html>
