from selenium import webdriver
#import chromedriver_autoinstaller
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
from openpyxl import Workbook
import time
import pandas as pd

#--------------------------------------------------------------------------------------------
#xpath가 존재하는지 확인
def isExistXpath(xpath, implicitly_wait_time=0, old_wait=25):
    driver.implicitly_wait(implicitly_wait_time)
    try:
        driver.find_element(By.XPATH, xpath)
    except Exception:
        return False
    finally:
        driver.implicitly_wait(old_wait)
    return True

#--------------------------------------------------------------------------------------------
  
def Crawling(num, page):
    with_before = 0
    
    for i in range(len(driver.find_elements(By.CSS_SELECTOR, '.article'))):
        articles = driver.find_elements(By.CSS_SELECTOR, 'a.article')[i]
        articles.click()
        time.sleep(1)
        
        product_date = driver.find_element(By.CSS_SELECTOR, 'span.date').text
        product_date_str = product_date.split('.')
        del product_date_str[-1]
        product_date = '.'.join(product_date_str)
        
        try:
            product_price_str = driver.find_element(By.CSS_SELECTOR, 'strong.cost').text
            product_price_str = product_price_str[:-1]
            product_price_str = product_price_str.replace(',', '')
            product_price = int(product_price_str)
        except:
            product_price = ''
        
        prod_price_total.append([product_date, product_price])
        
        driver.back()
        driver.switch_to.frame('cafe_main')
    
    cur_path = '//*[@id="main-area"]/div[7]/a[{}]'.format(num+page+1)
    driver.find_element(By.XPATH, cur_path).click()

#-------------------------------------------------------------------------------------------- 
   
options = Options()
options.add_argument('headless'); # headless는 화면이나 페이지 이동을 표시하지 않고 동작하는 모드

#chromedriver_autoinsttraller.install()
driver = webdriver.Chrome(options=options)
driver.implicitly_wait(5)
driver.set_window_size(1920,1280)

#크롤링 시작
url = 'https://cafe.naver.com/joonggonara'
driver.get(url)

product = ''
excepts = '삽니다, 매입, 구매, 구입'  # 1개라도 포함되면 안됨

name = input('이름 입력 : ')
product = product + name

mem = input('몇 기가 ? : ')
product = product + ' ' + mem

if '럭시' in product:
    if '플러스' or 'plus' not in product:
        excepts = excepts + ', 플러스, plus, +'
    if '울트라' or 'ultra' not in product:
        excepts = excepts + ', 울트라, ultra'
elif '이폰' in product:
    if '프로' or 'pro' not in product:
        excepts = excepts + ', 프로, pro'
    if '맥스' or 'max' not in product:
        excepts = excepts + ', 맥스, max'
    if '미니' or 'mini' not in product:
        excepts = excepts + ', 미니, mini'
    if '플러스' or 'plus' not in product:
        excepts = excepts + ', 플러스, plus'

#엑셀
wb = Workbook()
wb.create_sheet('{}'.format(product), 0)
prod_price_total = wb.active
prod_price_total.append(['날짜','가격'])

#검색
driver.find_element(By.CSS_SELECTOR, '#topLayerQueryInput').send_keys(product)
driver.find_element(By.CSS_SELECTOR, '#cafe-search .btn').click()
time.sleep(1)

driver.switch_to.frame('cafe_main')

#한달간, 핸드폰 설정
driver.find_element(By.CSS_SELECTOR,'#currentSearchDateTop').click()
time.sleep(1)
driver.find_element(By.XPATH,'//*[@id="select_list"]/li[6]').click()
time.sleep(1)
driver.find_element(By.CSS_SELECTOR,'#currentSearchMenuTop').click()
time.sleep(1)
driver.find_element(By.XPATH, '//*[@id="divSearchMenuTop"]/ul/li[21]').click()
time.sleep(1)

#상세 설정
driver.find_element(By.CSS_SELECTOR,'#detailSearchBtn').click()
time.sleep(1)
driver.find_element(By.XPATH,'//*[@id="srch_detail"]/div[2]/input').send_keys(excepts)
driver.find_element(By.CSS_SELECTOR,'.btn-search-green').click()
time.sleep(1)

#팬매완료도 보기
driver.find_element(By.XPATH,'//*[@id="searchOptionSelectDiv"]/a').click()
time.sleep(1)
driver.find_element(By.XPATH,'//*[@id="searchOptionSelectDiv"]/ul/li[1]/a').click()
time.sleep(1)

#전체 페이지 확인
cur_page = 1

num = 0
page_break = True
while 1:
    if num == 0:
        if isExistXpath('//*[@id="main-area"]/div[7]/a[11]'):
            driver.find_element(By.XPATH,'.//*[@id="main-area"]/div[7]/a[11]').click()
            time.sleep(1)
            num += 10
        else:
            for i in reversed(range(11)):
                page_xpath = '//*[@id="main-area"]/div[7]/a[' + str(i+1) + ']'
                if isExistXpath(page_xpath):
                    page = i+1
                    page_break = False
                    break
    else:
        if isExistXpath('//*[@id="main-area"]/div[7]/a[12]'):
            driver.find_element(By.XPATH,'.//*[@id="main-area"]/div[7]/a[12]').click()
            time.sleep(1)
            num += 10
        else:
            for i in reversed(range(11)):
                page_xpath = '//*[@id="main-area"]/div[7]/a[' + str(i+1) + ']'
                if isExistXpath(page_xpath):
                    page = i
                    page_break = False
                    break
    if page_break == False:
        break
total_page = page + num
total_next_page = total_page // 10
last_page = total_page - total_next_page * 10

#다시 처음으로
for i in reversed(range(total_next_page)):
    driver.find_element(By.XPATH,'.//*[@id="main-area"]/div[7]/a[1]').click()
driver.find_element(By.XPATH,'.//*[@id="main-area"]/div[7]/a[1]').click()

#전체 페이지 크롤링
print('----- Total Page : {}'.format(total_page), '------')

if total_next_page == 0:
    for page in range(total_page):
        print('----- Current Page : {}'.format(cur_page), '------')
        Crawling(0, page)
        cur_page += 1
        if cur_page > total_page:
            print('Crawling succeed!')
        
else:
    for n in range(total_next_page+1):
        if n == 0:
            for page in range(10):
                print('----- Current Page : {}'.format(cur_page), '------')
                Crawling(0, page)
                cur_page += 1
            
        elif n > 0 and n != total_next_page:
            for page in range(10):
                print('----- Current Page : {}'.format(cur_page), '------')
                Crawling(1, page)
                cur_page += 1
            
        elif n == total_next_page:
            for page in range(last_page):
                print('----- Current Page : {}'.format(cur_page), '------')
                Crawling(1, page)
                cur_page += 1
                if cur_page > total_page:
                    print('Crawling succeed!')

driver.quit()
product = product.replace(' ', '_')
wb.save(f'./file/joonggonara_crwling_{product}_price.xlsx')
