#!/home/admin/opt/python/bin/python
# -*- coding: utf-8 -*-

import MySQLdb
from LoggerFactory import LoggerFactory

class ScoreModel:
    sqls = {
            "getMinMaxAuthor":
            """
            SELECT min(author_id),max(author_id)
            FROM author
            """,
            "getResourceByAuthorId":
            """
            SELECT r_pv,r_category,r_gmt_create
            FROM resources
            WHERE author_id=%s
            """,
            "updateAuthorScore":
            """
            UPDATE author
            SET resource_num = %s,author_category = %s,score = %s
            WHERE author_id = %s
            """,
            "getMinMaxViewer":
            """
            SELECT min(viewer_id),max(viewer_id)
            FROM viewer
            """,
            "getResourceFloorByViewerId":
            """
            SELECT mfloor,r_comment,r_pv as r_num,r_category
            FROM (
                SELECT r_id,min(floor) as mfloor
                FROM viewtable
                WHERE viewer_id = %s
                GROUP BY r_id
            ) tab1
            LEFT JOIN resources
            ON tab1.r_id = resources.r_id
            """,
            "updateViewerScore":
            """
            UPDATE viewer
            SET score = %s,view_cnt = %s,viewer_category = %s
            WHERE viewer_id = %s
            """,
            "getMinMaxResourcesByDate":
            """
            SELECT r_id,author_id
            FROM resourcesgetMinMaxResourcesByDate
            WHERE r_gmt_create > %s and r_site = %s
            ORDER BY r_gmt_create desc
            """,
            "getAuthorScoreByRid":
            """
            SELECT author.score
            FROM (
                SELECT author_id
                FROM resources
                WHERE r_id = %s
            ) tab1
            LEFT JOIN author
            ON tab1.author_id = author.author_id
            """,
            "getResourcesRefViewerScoreByRid":
            """
            SELECT viewer.viewer_id,mfloor,score
            FROM (
                SELECT min(floor) as mfloor,viewer_id
                FROM viewtable
                WHERE r_id = %s
                GROUP BY viewer_id
                LIMIT %s
            ) tab1
            LEFT JOIN viewer
            ON tab1.viewer_id = viewer.viewer_id
            """,
            "updateResourceScore":
            """
            UPDATE resources
            SET score = %s
            WHERE r_id = %s
            """,
            "getAvgViewerScore":
            """
            SELECT avg(score)
            FROM viewer
            WHERE score>0
            """
            }
    
    def __init__(self, host, userName, password, dbName):
        self.LOGGER = LoggerFactory.getLogger("ScoreModel")
        self.host = host
        self.userName = userName
        self.password = password
        self.dbName = dbName
        
        self.dbConn = self._getConn()
        
    def __del__(self):
        if self.dbConn is None:
            self.dbConn.close()
        
    
    def _getConn(self):
        try:
            return MySQLdb.connect(
                self.host,
                self.userName,
                self.password,
                self.dbName,
                charset="utf8",
                port=int(3306)
            )
        except Exception, ex:
            self.LOGGER.error(ex)
            
            
    def getMinMaxAuthor(self):
        try:
            sql = self.sqls.get("getMinMaxAuthor")
            c = self.dbConn.cursor()
            n = c.execute(sql)
            rows = c.fetchall()
            return rows
        except Exception, ex:
            self.LOGGER.error(ex)
        
    def getResourceByAuthorId(self, authorId):
        try:
            sql = self.sqls.get("getResourceByAuthorId")
            c = self.dbConn.cursor()
            n = c.execute(sql, (authorId,))
            rows = c.fetchall()
            return rows
        except Exception, ex:
            self.LOGGER.error(ex)
        
    def updateAuthorScore(self, authorId, score, resourceNum, category):
        try:
            sql = self.sqls.get("updateAuthorScore")
            c = self.dbConn.cursor()
            n = c.execute(sql, (resourceNum, category, score, authorId))
            self.dbConn.commit()
        except Exception, ex:
            self.LOGGER.error(ex)
            
    def getMinMaxViewer(self):
        try:
            sql = self.sqls.get("getMinMaxViewer")
            c = self.dbConn.cursor()
            n = c.execute(sql)
            rows = c.fetchall()
            return rows
        except Exception, ex:
            self.LOGGER.error(ex)
    
    def getResourceFloorByViewerId(self, viewerId):
        try:
            sql = self.sqls.get("getResourceFloorByViewerId")
            c = self.dbConn.cursor()
            n = c.execute(sql, (viewerId,))
            rows = c.fetchall()
            return rows
        except Exception, ex:
            self.LOGGER.error(ex)
            
    def updateViewerScore(self, viewerId, score, resourceNum, category):
        try:
            sql = self.sqls.get("updateViewerScore")
            c = self.dbConn.cursor()
            n = c.execute(sql, (score, resourceNum, category, viewerId))
            self.dbConn.commit()
        except Exception, ex:
            self.LOGGER.error(ex)
            
    def getResourcesByDate(self,oldDate,rSite):
        try:
            sql = self.sqls.get("getMinMaxResourcesByDate")
            c = self.dbConn.cursor()
            n = c.execute(sql, (oldDate,rSite))
            rows = c.fetchall()
            return rows
        except Exception, ex:
            self.LOGGER.error(ex)
    
    def getAuthorScoreByRid(self,rId):
        try:
            sql = self.sqls.get("getAuthorScoreByRid")
            c = self.dbConn.cursor()
            n = c.execute(sql, (rId,))
            rows = c.fetchall()
            return rows
        except Exception, ex:
            self.LOGGER.error(ex)
            
    def getResourcesRefViewerScoreByRid(self,rId,limitNum):
        try:
            sql = self.sqls.get("getResourcesRefViewerScoreByRid")
            c = self.dbConn.cursor()
            n = c.execute(sql, (rId,limitNum))
            rows = c.fetchall()
            return rows
        except Exception, ex:
            self.LOGGER.error(ex)
            
    def updateResourceScore(self,rId,score):
        try:
            sql = self.sqls.get("updateResourceScore")
            c = self.dbConn.cursor()
            n = c.execute(sql, (score, rId))
            self.dbConn.commit()
        except Exception, ex:
            self.LOGGER.error(ex)
            
    def getAvgViewerScore(self):
        try:
            sql = self.sqls.get("getAvgViewerScore")
            c = self.dbConn.cursor()
            n = c.execute(sql)
            rows = c.fetchall()
            return rows
        except Exception,ex:
            self.LOGGER.error(ex)