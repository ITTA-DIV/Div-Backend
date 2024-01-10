import requests
from bs4 import BeautifulSoup
import pymysql
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
import re
from urllib.request import urlopen
from config import DB_HOST, DB_USER, DB_PASSWORD, DB_NAME

# 시작날짜, 끝나는 날짜 분리 함수
def parse_date_data(date_data):
    if(len(date_data)>30):
        dates = date_data.split(" ~ ")
        numbers_only = re.sub(r'\D', '', dates[0])
        if(len(numbers_only)==7):numbers_only="0"+numbers_only
        month=numbers_only[:2]
        if(int(month)>10): year="2023"
        else: year="2024"
        day=numbers_only[2:4]
        hour=numbers_only[4:6]
        minute=numbers_only[6:8]
        startdate=year+"-"+month+"-"+day+" "+hour+":"+minute+":00"
#         print(startdate)

        numbers_only = re.sub(r'\D', '', dates[1])
        if(len(numbers_only)==7):numbers_only="0"+numbers_only
        month=numbers_only[:2]
        if(int(month)>10): year="2023"
        else: year="2024"
        day=numbers_only[2:4]
        hour=numbers_only[4:6]
        minute=numbers_only[6:8]
        enddate=year+"-"+month+"-"+day+" "+hour+":"+minute+":00"
#         print(enddate)


    else:
        dates = date_data.split(" ~ ")
        numbers_only = re.sub(r'\D', '', dates[0])
        if(len(numbers_only)==7):numbers_only="0"+numbers_only
        month=numbers_only[:2]
        if(int(month)>10): year="2023"
        else: year="2024"
        day=numbers_only[2:4]
        hour=numbers_only[4:6]
        minute=numbers_only[6:8]
        startdate=year+"-"+month+"-"+day+" "+hour+":"+minute+":00"
#         print(startdate)

        numbers_only = re.sub(r'\D', '', dates[1])
        hour=numbers_only[:2]
        minute=numbers_only[2:4]
        enddate=year+"-"+month+"-"+day+" "+hour+":"+minute+":00"
#         print(enddate)
    return startdate, enddate


def crawl_page(page):
    url = "https://event-us.kr/search?order=created&date=%EB%AA%A8%EB%93%A0%EB%82%A0&page="+page

    service = Service(executable_path='/home/ubuntu/chromedriver-linux64/chromedriver')
    chrome_options = Options()
    chrome_options.add_argument("--headless")

    # linux 환경에서 필요한 option
    chrome_options.add_argument('--no-sandbox')
    chrome_options.add_argument('--disable-dev-shm-usage')
    driver = webdriver.Chrome(service=service, options=chrome_options)

    # 웹 페이지 로드
    driver.get(url)

    # Selenium으로 페이지 스크랩
    rendered_html = driver.page_source

    # BeautifulSoup을 사용하여 HTML 파싱
    soup = BeautifulSoup(rendered_html, 'html.parser')

    events=soup.findAll('a',{'class':'aspect-h-9'})

    count=0

    tuples=[]
    for event in events:
        print("----------------------------")
#         if(cnt>3):
# #             print("충분히 업데이트 된 것 같습니다")
#             return -1
        href_value = event.get('href')
        driver.get(f"https://event-us.kr/{href_value}")
        new_page_html = driver.page_source
        new_page_soup = BeautifulSoup(new_page_html, 'html.parser')

        type=new_page_soup.find('a', {'class': 'px-2 text-xs'}).text

        if(type=="강연/세미나"):
            type="세미나"
        elif(type=="모임/커뮤니티" or type=="멘토링/대외활동" or type=="회의/컨벤션"):
            type="커뮤니티"
        elif(type=="박람회/페어"):
            type="박람회"
        elif(type=="박람회/페어"):
            type="박람회"
        elif(type=="공연/전시"):
            type="전시회"
        else:
            continue

        title = new_page_soup.find('div', {'class': 'text-xl'}).text[1:]
        title = title.replace('\'', '')
        print(title)

#         cursor.execute("SELECT COUNT(*) FROM `events` WHERE title = %s", (title))
#         count = cursor.fetchone()[0]

#         if count > 0:
# #             print(f"'{title}' 이미 존재..")
#             cnt=cnt+1
#             continue

        infoSection=new_page_soup.find('section', {'id': 'infoSection'})

        # startdate, enddate 처리 - - - - - - - - - - -- - - - - - - - -
        date_data=infoSection.dl.find_all('div')[1].dd.span.text[1:]
#         print(date_data)
        startdate, enddate = parse_date_data(date_data)

#         print("startdate:", startdate)
#         print("enddate:", enddate)
        #  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        price=infoSection.dl.find_all('div')[2].dd.span.text
        price = price.replace(',', '')

        mapSection = new_page_soup.find('section', {'id': 'mapSection'})

        try:
            location=mapSection.dl.find_all('div')
            if location:
                location=location[0].dd.text.replace('\'', '')
            else:
                location = ""
        except IndexError:
            # IndexError가 발생할 경우에 대한 처리
            location = ""
            print("IndexError: 'div' element not found in the list.")

        # location을 사용 또는 출력
        print(location)


        address=mapSection.dl.find_all('div')
        if(len(address)>1):
            address=address[1].dd.div.text[1:].replace('\'', '')
        else:
            address=""
#         print(address)

        # 카테고리 외래키 처리 - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        category_str=new_page_soup.find('a', {'class': 'pr-2 text-xs'}).text

        category_mapping = {
            "창업": 1,
            "IT/프로그래밍": 2,
            "라이프": 3,
            "경제/금융": 4,
            "경영": 5,
            "인문/사회": 6,
            "예술": 7,
            "마케팅": 8,
            "커리어": 9,
            "과학기술": 10,
            "디자인/영상": 11,
            "의료/의학": 12,
            "행사 기획": 13,
            "관광/여행": 14,
            "기타":15
        }
        category_id=category_mapping[category_str]
        if category_id ==15:
            continue
#         print(category_id)


        #  - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - - -

        host=new_page_soup.find('div',{'class':'font-bold flex flex-col'}).text[1:]
#         print(host)

        host_profile=new_page_soup.find('img',{'class':'aspect-1 h-6 w-6 object-cover rounded-full border'})
        if host_profile is not None:
            host_profile=host_profile.get('src')
        else: # 이미지가 없는 경우
            host_profile=""
#         print(host_profile)

        link=new_page_soup.find('form',{'class':'space-y-4'}).get('action')
        link="https://event-us.kr"+link
#         print(link)

        applydate=infoSection.dl.find_all('div')[0].dd.span.text[1:]
        print(applydate)
        applystart, applyend = parse_date_data(applydate)

#         print("applystart:", applystart)
#         print("applyend:", applyend)


        thumbnail=new_page_soup.find('img',{'class':'w-full rounded-md border'}).get('src')
#         print(thumbnail)

        # is_permit 추가
        is_permit = 1

        count+=1

        tuple=(title,startdate,enddate,price,location,address,host,host_profile,link,applystart,applyend,type,category_id,thumbnail,is_permit)
        tuples.append(tuple)


    driver.quit()

    return count, tuples

def eventusCrawling():
    host_name = DB_HOST
    username = DB_USER
    password = DB_PASSWORD
    database_name = DB_NAME
    db = pymysql.connect(
        host=host_name,
        port=3306,
        user=username,
        passwd=password,
        db=database_name,
        charset='utf8'
    )
    # db에 연결
    cursor = db.cursor()
    cursor.execute("set names utf8")
    db.commit()
    # 엔티티 완성 후
    cursor.execute("USE demo;")
    db.commit()

    for i in range (1, 79):
        n=1
        page = str(i)
        n, result = crawl_page(page)

#         print(result)
        stmt = "INSERT IGNORE INTO `events` (title, start_date, end_date, price, location, address, host,host_profile, link, apply_start_date, apply_end_date, type, category_id, thumbnail, is_permit) VALUES (%s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s, %s, %s)"
        cursor.executemany(stmt, result)
        db.commit()
#         print(n)
#         print(cursor.rowcount)
        if cursor.rowcount+2 < n:
            break

    db.close()

eventusCrawling()
