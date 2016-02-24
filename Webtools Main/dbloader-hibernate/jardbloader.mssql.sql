/****** Object:  ForeignKey [FK_JarFileEntries_JarFiles]    Script Date: 03/16/2014 13:29:35 ******/
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_JarFileEntries_JarFiles]') AND parent_object_id = OBJECT_ID(N'[dbo].[JarFileEntries]'))
ALTER TABLE [dbo].[JarFileEntries] DROP CONSTRAINT [FK_JarFileEntries_JarFiles]
GO
/****** Object:  Table [dbo].[JarFileEntries]    Script Date: 03/16/2014 13:29:35 ******/
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_JarFileEntries_JarFiles]') AND parent_object_id = OBJECT_ID(N'[dbo].[JarFileEntries]'))
ALTER TABLE [dbo].[JarFileEntries] DROP CONSTRAINT [FK_JarFileEntries_JarFiles]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[JarFileEntries]') AND type in (N'U'))
DROP TABLE [dbo].[JarFileEntries]
GO
/****** Object:  Table [dbo].[JarFiles]    Script Date: 03/16/2014 13:29:35 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[JarFiles]') AND type in (N'U'))
DROP TABLE [dbo].[JarFiles]
GO
/****** Object:  Table [dbo].[JarFiles]    Script Date: 03/16/2014 13:29:35 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[JarFiles]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[JarFiles](
	[jid] [int] IDENTITY(1,1) NOT NULL,
	[jarData] [varbinary](max) NOT NULL,
	[jarName] [varchar](50),
 CONSTRAINT [PK_JarFiles] PRIMARY KEY CLUSTERED 
(
	[jid] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[JarFileEntries]    Script Date: 03/16/2014 13:29:35 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[JarFileEntries]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[JarFileEntries](
	[entryName] [varchar](100) NOT NULL,
	[jid] [int] NOT NULL,
 CONSTRAINT [PK_JarFileEntries] PRIMARY KEY CLUSTERED 
(
	[entryName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  ForeignKey [FK_JarFileEntries_JarFiles]    Script Date: 03/16/2014 13:29:35 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_JarFileEntries_JarFiles]') AND parent_object_id = OBJECT_ID(N'[dbo].[JarFileEntries]'))
ALTER TABLE [dbo].[JarFileEntries]  WITH CHECK ADD  CONSTRAINT [FK_JarFileEntries_JarFiles] FOREIGN KEY([jid])
REFERENCES [dbo].[JarFiles] ([jid])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_JarFileEntries_JarFiles]') AND parent_object_id = OBJECT_ID(N'[dbo].[JarFileEntries]'))
ALTER TABLE [dbo].[JarFileEntries] CHECK CONSTRAINT [FK_JarFileEntries_JarFiles]
GO
