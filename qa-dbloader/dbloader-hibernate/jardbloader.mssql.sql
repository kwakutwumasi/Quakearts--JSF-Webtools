/****** Object:  ForeignKey [FK_jar_file_entries_jar_files]    Script Date: 03/16/2014 13:29:35 ******/
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_jar_file_entries_jar_files]') AND parent_object_id = OBJECT_ID(N'[dbo].[jar_file_entries]'))
ALTER TABLE [dbo].[jar_file_entries] DROP CONSTRAINT [FK_jar_file_entries_jar_files]
GO
/****** Object:  Table [dbo].[jar_file_entries]    Script Date: 03/16/2014 13:29:35 ******/
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_jar_file_entries_jar_files]') AND parent_object_id = OBJECT_ID(N'[dbo].[jar_file_entries]'))
ALTER TABLE [dbo].[jar_file_entries] DROP CONSTRAINT [FK_jar_file_entries_jar_files]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[jar_file_entries]') AND type in (N'U'))
DROP TABLE [dbo].[jar_file_entries]
GO
/****** Object:  Table [dbo].[jar_files]    Script Date: 03/16/2014 13:29:35 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[jar_files]') AND type in (N'U'))
DROP TABLE [dbo].[jar_files]
GO
/****** Object:  Table [dbo].[jar_files]    Script Date: 03/16/2014 13:29:35 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[jar_files]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[jar_files](
	[jid] [int] IDENTITY(1,1) NOT NULL,
	[jar_data] [varbinary](max) NOT NULL,
	[jar_name] [varchar](50),
 CONSTRAINT [PK_jar_files] PRIMARY KEY CLUSTERED 
(
	[jid] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[jar_file_entries]    Script Date: 03/16/2014 13:29:35 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[jar_file_entries]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[jar_file_entries](
	[entry_name] [varchar](100) NOT NULL,
	[jid] [int] NOT NULL,
 CONSTRAINT [PK_jar_file_entries] PRIMARY KEY CLUSTERED 
(
	[entry_name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  ForeignKey [FK_jar_file_entries_jar_files]    Script Date: 03/16/2014 13:29:35 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_jar_file_entries_jar_files]') AND parent_object_id = OBJECT_ID(N'[dbo].[jar_file_entries]'))
ALTER TABLE [dbo].[jar_file_entries]  WITH CHECK ADD  CONSTRAINT [FK_jar_file_entries_jar_files] FOREIGN KEY([jid])
REFERENCES [dbo].[jar_files] ([jid])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_jar_file_entries_jar_files]') AND parent_object_id = OBJECT_ID(N'[dbo].[jar_file_entries]'))
ALTER TABLE [dbo].[jar_file_entries] CHECK CONSTRAINT [FK_jar_file_entries_jar_files]
GO
