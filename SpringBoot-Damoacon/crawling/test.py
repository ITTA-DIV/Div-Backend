import sys
import requests
from bs4 import BeautifulSoup
import pandas as pd
import pymysql
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
import re
import json

def generate_json_list():
    # 빈 리스트 생성
    data_list = []

    # JSON 리스트를 100번 반복하여 추가
    for i in range(100):
        data_list.append({"key": f"value{i}", "number": i})

    # JSON 형식으로 변환
    json_list = json.dumps(data_list)
    return json_list

# 함수 호출하여 JSON 리스트 생성
result = generate_json_list()

# 생성된 JSON 리스트 출력
print(result)