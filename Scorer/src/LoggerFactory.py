#!/home/admin/opt/python/bin/python
# -*- coding: utf-8 -*-
import logging

class LoggerFactory(object):
    @staticmethod
    def getLogger(loggerName):
        LOGGER = logging.getLogger(loggerName)
        LOGGER.setLevel(logging.INFO)
        
        formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
        
        fh = logging.FileHandler('logs/score_info.log')
        fh.setLevel(logging.INFO)
        fh.setFormatter(formatter)
        
        LOGGER.addHandler(fh)
        
        ch = logging.FileHandler('logs/score_error.log')
        ch.setLevel(logging.ERROR)
        ch.setFormatter(formatter)
        
        LOGGER.addHandler(ch)
        
        return LOGGER
    
if __name__ == "__main__":
    LOGGER = LoggerFactory.getLogger("TEST")
    LOGGER.info("infoTest")
    LOGGER.error("errorTest")
