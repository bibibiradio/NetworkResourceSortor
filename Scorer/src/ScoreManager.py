#!/home/admin/opt/python/bin/python
# -*- coding: utf-8 -*-
from Scorer import Scorer
from LoggerFactory import LoggerFactory
import time

class ScoreManager(object):
    everyRunDelayTime = 120.0
    
    def __init__(self):
        self.LOGGER = LoggerFactory.getLogger("ScoreManager")
        self.scorer =Scorer()
        self.round = 1
        
    def run(self):
        while True:
            try:
                startTime = time.time()
                self.LOGGER.info("round "+str(self.round))
                
                try:
                    #self.scorer.viewerScore()
                    #self.scorer.authorScore()
                    self.scorer.resourceScore()
                except Exception,ex:
                    self.LOGGER.error(ex)
                
                endTime = time.time()
                self.round += 1 
                
                updateTime = endTime - startTime
                #if updateTime/10 > self.everyRunDelayTime:
                #    self.everyRunDelayTime = updateTime/10
                    
                self.LOGGER.info( "score round use "+str(updateTime)+" s ,next score delay "+str(self.everyRunDelayTime)+" s")
                time.sleep(self.everyRunDelayTime)
            except Exception,ex:
                self.LOGGER.error(ex)
        
if __name__ == "__main__":
    scoreManager = ScoreManager()
    scoreManager.run()
    
        
