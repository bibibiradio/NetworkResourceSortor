#!/home/admin/opt/python/bin/python
# -*- coding: utf-8 -*-

from ScoreModel import ScoreModel
from LoggerFactory import LoggerFactory
import time
import datetime
import Queue
import threadpool
import thread

class Scorer():
    """ """
    def __init__(self):
        Scorer.LOGGER = LoggerFactory.getLogger("Scorer")
        
        Scorer.updateViewerScoreNum = 0
        
        Scorer.updateResourceNum = 0
        Scorer.updateViewerNum = 0
        Scorer.updateAuthorNum = 0
        Scorer.updateResourceTime = 0
        Scorer.updateViewerTime = 0
        Scorer.updateAuthorTime = 0
        Scorer.dupAuthor = 0
        Scorer.dupViewer = 0
        Scorer.avgViewerScore = 0
        
        Scorer.updateNumLock = thread.allocate_lock()
        
        Scorer.authorHashSet = dict()
        Scorer.viewerHashSet = dict()
        Scorer.cacheLimitDay = 30
        
        self.mysqlHost = "10.163.102.88"
        self.mysqlUserName = "resource_owner"
        self.mysqlPassword = "123456"
        self.mysqlDb = "networkresourcesort"
        
        self.viewerScoreThreadPool = None
        
        Scorer.scoreModelPool = Queue.Queue(10)
        for i in xrange(0, 7):
            Scorer.scoreModelPool.put(ScoreModel(self.mysqlHost, self.mysqlUserName, self.mysqlPassword, self.mysqlDb))
    
    @staticmethod
    def authorScoreOne(authorId):
        try:
            sumPv = 0
            resourceNum = 0
            categoryMap = dict()
            maxCategoryNum = 0
            maxCategory = ""
            scoreModel = None
            rows = None
            
            try:
                scoreModel = Scorer.scoreModelPool.get()
                rows = scoreModel.getResourceByAuthorId(authorId);
            finally:
                Scorer.scoreModelPool.put(scoreModel)
                
            if(rows is None or len(rows) <= 0):
                return
            
            
            for row in rows:
                sumPv += row[0]
                resourceNum += 1
                category = row[1]
                if categoryMap.get(row[1]) is None:
                    categoryMap[row[1]] = 1
                else:
                    categoryMap[row[1]] += 1
                if categoryMap[row[1]] > maxCategoryNum:
                    maxCategory = category
                    maxCategoryNum = categoryMap[row[1]]
            score = sumPv / len(rows)
            
            try:
                scoreModel = Scorer.scoreModelPool.get()
                scoreModel.updateAuthorScore(authorId, score, resourceNum, maxCategory)
            finally:
                Scorer.scoreModelPool.put(scoreModel)
            
        except Exception, ex:
            Scorer.LOGGER.error(ex)
            
    def authorScore(self):
        startTime = time.time()
        rows = None
        scoreModel = None
        
        try:
            scoreModel = self.scoreModelPool.get()
            rows = scoreModel.getMinMaxAuthor()
        finally:
            self.scoreModelPool.put(scoreModel)
            
        if len(rows) <= 0:
            self.LOGGER.error("rows not good")
            return
        
        minAuthorId = rows[0][0]
        maxAuthorId = rows[0][1]
        
        updateNum = 0
        for authorId in xrange(minAuthorId, maxAuthorId):
            try:
                Scorer.authorScoreOne(authorId)
                updateNum += 1
                
                if updateNum != 0 and updateNum % 1000 == 0:
                    self.LOGGER.info("update " + str(updateNum) + " author")
            except Exception, ex:
                self.LOGGER(ex)
        endTime = time.time()
        
        self.LOGGER.info("author score update,all update " + str(updateNum) + ",use " + str(endTime - startTime) + " s")
    
    @staticmethod
    def viewerScoreOne(viewerId):
        try:
            rows = None
            scoreModel = None
            
            try:
                scoreModel = Scorer.scoreModelPool.get()
                rows = scoreModel.getResourceFloorByViewerId(viewerId)
            finally:
                Scorer.scoreModelPool.put(scoreModel)
            
            if(rows is None or len(rows) <= 0):
                return
            
            resourceNum = 0
            sumScore = 0
            categoryMap = dict()
            maxCategoryNum = 0
            maxCategory = ""
                
            
            for row in rows:
                mFloor = row[0]
                commentNum = row[1]
                pv = row[2]
                
                if commentNum <= 0:
                    continue
                
                resourceNum += 1
                ascore = pv * (1 - (float(mFloor) / commentNum))
                sumScore += ascore
                
                category = row[3]
                if categoryMap.get(row[3]) is None:
                    categoryMap[row[3]] = 1
                else:
                    categoryMap[row[3]] += 1
                if categoryMap[row[3]] > maxCategoryNum:
                    maxCategory = category
                    maxCategoryNum = categoryMap[row[3]]
            
            if resourceNum > 0:
                score = sumScore / resourceNum
            else:
                score = 0
                
            try:
                scoreModel = Scorer.scoreModelPool.get()
                scoreModel.updateViewerScore(viewerId, score, resourceNum, maxCategory)
            finally:
                Scorer.scoreModelPool.put(scoreModel)
                
        except Exception, ex:
            Scorer.LOGGER.error(ex)
        finally:
            Scorer.updateViewerScoreNum += 1
            #if Scorer.updateViewerScoreNum % 50 == 0:
            #    Scorer.LOGGER.info("viewerScoreOne update %s viewer scorer" % Scorer.updateViewerScoreNum)
        
    def viewerScore(self):
        startTime = time.time()
        rows = None
        scoreModel = None
        
        if self.viewerScoreThreadPool is None:
            self.viewerScoreThreadPool = threadpool.ThreadPool(4)
            
            
        try:
            scoreModel = self.scoreModelPool.get()
            rows = scoreModel.getMinMaxViewer()
        finally:
            self.scoreModelPool.put(scoreModel)
            
        if rows is None or len(rows) <= 0:
            self.LOGGER.error("get MIN MAX Viewer ERROR")
            return
            
        minViewerId = rows[0][0]
        maxViewerId = rows[0][1]
        if(rows is None or len(rows) <= 0):
            self.LOGGER.error("rows not good")
            return
        
        reqList = []
        for viewerId in xrange(minViewerId, maxViewerId):
            if len(reqList) > 100:
                threadRequests = threadpool.makeRequests(Scorer.viewerScoreOne, reqList)
                for req in threadRequests:
                    self.viewerScoreThreadPool.putRequest(req)
                self.viewerScoreThreadPool.wait()
                reqList = []
            else:
                reqList.append(viewerId)
                
        
        
            
        
        '''
        updateNum = 0
        for viewerId in xrange(minViewerId, maxViewerId):
            try:
                Scorer.viewerScoreOne(viewerId)
                updateNum += 1
                if updateNum != 0 and updateNum % 1000 == 0:
                    self.LOGGER.info("update " + str(updateNum) + " viewer")
            except Exception, ex:
                self.LOGGER.error(ex)
        '''    
        endTime = time.time()
        updateTime = endTime - startTime
        
        self.LOGGER.info("viewer score update,all update " + str(Scorer.updateViewerScoreNum) + ",use " + str(updateTime) + " s")
    
    @staticmethod
    def resourceScoreOne(rIdTuple):
        rows = None
        scoreModel = None
        innerUpdateAuthorTime = 0
        innerUpdateViewerTime = 0
        innerUpdateResTime = 0
        innerDupAuthor = 0
        innerDupViewer = 0
        innerUpdateViewerNum = 0
        try:
            
            sResTime = time.time()
            rId = rIdTuple[0]
            authorId = rIdTuple[1]
            
            authorCacheTime = Scorer.authorHashSet.get(authorId)
            if authorCacheTime is None:
                sAuthorTime = time.time()
                Scorer.authorScoreOne(authorId)
                try:
                    Scorer.updateNumLock.acquire()
                    Scorer.authorHashSet[authorId] = time.time()
                finally:
                    Scorer.updateNumLock.release()
                innerUpdateAuthorTime += time.time() - sAuthorTime
            else:
                if time.time() - authorCacheTime > Scorer.cacheLimitDay * 24 * 60 * 60:
                    sAuthorTime = time.time()
                    Scorer.authorScoreOne(authorId)
                    try:
                        Scorer.updateNumLock.acquire()
                        Scorer.authorHashSet[authorId] = time.time()
                    finally:
                        Scorer.updateNumLock.release()
                    innerUpdateAuthorTime += time.time() - sAuthorTime
                else:
                    innerDupAuthor += 1
            
            
            try:
                scoreModel = Scorer.scoreModelPool.get()
                rows = scoreModel.getAuthorScoreByRid(rId)
            finally:
                Scorer.scoreModelPool.put(scoreModel)
                
            if rows is None or len(rows) <= 0:
                #Scorer.LOGGER.error("getAuthorScoreByRid exe fail %s rIs!"%(rId,))
                return
            
            authorScore = rows[0][0]
            recordViewerNum = 0
            viewerScore = 0
            score = 0
            
            try:
                scoreModel = Scorer.scoreModelPool.get()
                rows = scoreModel.getResourcesRefViewerScoreByRid(rId, 50)
            finally:
                Scorer.scoreModelPool.put(scoreModel)
                
            if rows is None or len(rows) <= 0:
                #Scorer.LOGGER.error("getResourcesRefViewerScoreByRid exe fail %s rId!"%(rId,))
                return
            
            dealViewerNum = 0
            for row in rows:
                viewerId = row[0]
                
                cacheViewerTime = Scorer.viewerHashSet.get(viewerId)
                if cacheViewerTime is None:
                    sViewerTime = time.time()
                    Scorer.viewerScoreOne(row[0])
                    try:
                        Scorer.updateNumLock.acquire()
                        Scorer.viewerHashSet[viewerId] = time.time()
                    finally:
                        Scorer.updateNumLock.release()
                    innerUpdateViewerTime += time.time() - sViewerTime
                else:
                    if time.time() - cacheViewerTime > Scorer.cacheLimitDay * 24 * 60 * 60:
                        sViewerTime = time.time()
                        Scorer.viewerScoreOne(row[0])
                        try:
                            Scorer.updateNumLock.acquire()
                            Scorer.viewerHashSet[viewerId] = time.time()
                        finally:
                            Scorer.updateNumLock.release()
                        innerUpdateViewerTime += time.time() - sViewerTime
                    else:
                        innerDupViewer += 1
                innerUpdateViewerNum += 1
                
                
                if dealViewerNum < 10:
                    vascore = row[2]
                    viewerScore += vascore
                    recordViewerNum += 1
                dealViewerNum += 1
            
            #score = (authorScore + viewerScore) / (recordViewerNum + 1)
            score = (authorScore + viewerScore + (10 - len(rows))*Scorer.avgViewerScore) / (10 + 1)
            
            try:
                scoreModel = Scorer.scoreModelPool.get()
                scoreModel.updateResourceScore(rId, score)
            finally:
                Scorer.scoreModelPool.put(scoreModel)
                
            innerUpdateResTime += time.time() - sResTime
            
            try:
                Scorer.updateNumLock.acquire()
                Scorer.updateResourceNum += 1
                Scorer.updateAuthorNum += 1
                Scorer.updateViewerNum += innerUpdateViewerNum
                Scorer.dupAuthor += innerDupAuthor
                Scorer.dupViewer += innerDupViewer
                Scorer.updateAuthorTime += innerUpdateAuthorTime
                Scorer.updateViewerTime += innerUpdateViewerTime
                Scorer.updateResourceTime += innerUpdateResTime
                if Scorer.updateResourceNum != 0 and Scorer.updateResourceNum % 1000 == 0:
                    Scorer.LOGGER.info("update %s resources / %s s,%s author / %s s,%s viewer / %s s,dup %s author,%s viewer,per res time %s,per viewer time %s,per author time %s" % (str(Scorer.updateResourceNum), str(Scorer.updateResourceTime), str(Scorer.updateAuthorNum), str(Scorer.updateAuthorTime), str(Scorer.updateViewerNum), str(Scorer.updateViewerTime), str(Scorer.dupAuthor), str(Scorer.dupViewer),str(Scorer.updateResourceTime/Scorer.updateResourceNum),str(Scorer.updateViewerTime/Scorer.updateViewerNum),str(Scorer.updateAuthorTime/Scorer.updateAuthorNum)))
            finally:
                Scorer.updateNumLock.release()
                
        except Exception, ex:
            Scorer.LOGGER.error(ex)
            
        
            
    
    def resourceScore(self):
        startTime = time.time()
        rows = None
        scoreModel = None
        rowsrId = None
        resourcesScoreThreadPool = None
        
        try:
            scoreModel = Scorer.scoreModelPool.get()
            rows = scoreModel.getAvgViewerScore()
        finally:
            Scorer.scoreModelPool.put(scoreModel)
        
        if rows is None or len(rows) != 1:
            self.LOGGER.error("get avg viewer score fail")
            return
        self.avgViewerScore = rows[0][0]
        
        oldDataLimit = datetime.datetime.now() - datetime.timedelta(days=21)
        
        try:
            scoreModel = self.scoreModelPool.get()
            rowsrId = scoreModel.getResourcesByDate(oldDataLimit)
        finally:
            self.scoreModelPool.put(scoreModel)
            
        if rowsrId is None or len(rowsrId) <= 0:
            self.LOGGER.error("get Resources Error")
            return
        
        self.LOGGER.info(str(len(rowsrId)) + " resources Need Update")
        
        if resourcesScoreThreadPool is None:
            resourcesScoreThreadPool = threadpool.ThreadPool(8)
        
        reqList = []
        for rIdTuple in rowsrId:
            if len(reqList) > 100:
                threadRequests = threadpool.makeRequests(Scorer.resourceScoreOne, reqList)
                for req in threadRequests:
                    resourcesScoreThreadPool.putRequest(req)
                resourcesScoreThreadPool.wait()
                reqList = []
            else:
                reqList.append(([rIdTuple],None))
        
        resourcesScoreThreadPool.dismissWorkers(8, do_join=True)
        
        endTime = time.time()
        updateTime = endTime - startTime
        self.LOGGER.info("resource score update,all update " + str(Scorer.updateResourceNum) + ",use " + updateTime + " s")
            
        
        

if __name__ == "__main__":
    score = Scorer()
    score.resourceScore()
    score.authorScore()
    score.viewerScore()
