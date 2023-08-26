from selenium import webdriver
#import chromedriver_autoinstaller
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.keys import Keys
from openpyxl import Workbook
import time
import pandas as pd
import pyperclip

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

def isExistCSS(CSS_selector, implicitly_wait_time=0, old_wait=25):
    driver.implicitly_wait(implicitly_wait_time)
    try:
        driver.find_element(By.CSS_SELECTOR, xpath)
    except Exception:
        return False
    finally:
        driver.implicitly_wait(old_wait)
    return True

#상품명 리스트 뽑기
def get_product_list():
    teblit_df = pd.read_excel('./file/danawa_crawling_tablit_detail_result_class.xlsx')
    teblit_df = teblit_df.astype('string')
    teblit_df['내장메모리'] = teblit_df['내장메모리'].fillna('')
    global teblit_name
    global teblit_mem
    global add_word
    teblit_name = []
    teblit_mem = []
    add_word = []
    
    teblit_name_list = teblit_df['Product_name']
    teblit_mem_list = teblit_df['내장메모리']
    
    i = 0

    for n in teblit_name_list:
        i = 0
        teblit_name_single = n.split()
        for word in reversed(teblit_name_single):
            if 'GB' in word:
                teblit_name_single.remove(word)
            if '엘로' in word:
                teblit_name_single.remove(word)
            if '그린' in word:
                teblit_name_single.remove(word)
            if '퍼플' in word:
                teblit_name_single.remove(word)
            if '블루' in word:
                teblit_name_single.remove(word)
            if '레드' in word:
                teblit_name_single.remove(word)
            if '블랙' in word:
                teblit_name_single.remove(word)
            if '화이트' in word:
                teblit_name_single.remove(word)
            if '로즈' in word:
                teblit_name_single.remove(word)
            if '알파인' in word:
                teblit_name_single.remove(word)
            if 'Wi-Fi' in word:
                add_word.append('wifi, 와이파이')
                teblit_name_single.remove(word)
                i=1
            elif 'LTE' in word:
                add_word.append('LTE')
                teblit_name_single.remove(word)
                i=1
            elif '5G' in word:
                add_word.append('5G')
                teblit_name_single.remove(word)
                i=1
        if not i:
            add_word.append(' ')

        teblit_name_single = ' '.join(teblit_name_single)
        teblit_name.append(teblit_name_single)

    for m in teblit_mem_list:
        if m is not None:
            teblit_mem_single = m.replace("GB", "").replace("TB", "")
        teblit_mem.append(teblit_mem_single)
    
    print('----- 상품 갯수 : {}'.format(len(teblit_name)), '------')

#검색
def search_name(product):
    driver.find_element(By.CSS_SELECTOR, '#topLayerQueryInput').send_keys(product)
    driver.find_element(By.CSS_SELECTOR, '#cafe-search .btn').click()
    time.sleep(1)

#except 설정
def set_except(product):
    excepts = '삽니다, 매입, 구매, 구입, 교환'
    
    if '럭시' in product:
        if ('플러스' or 'plus') not in product:
            excepts = excepts + ', 플러스, plus, +'
        if ('울트라' or 'ultra') not in product:
            excepts = excepts + ', 울트라, ultra'
        if ('라이트' or 'lite') in product:
            excepts = excepts + ', 라이트, lite'
        if 'FE' in product:
            excepts = excepts + ', FE'
        
    elif '이폰' in product:
        if ('프로' or 'pro') not in product:
            excepts = excepts + ', 프로, pro'
        if ('에어' or 'air') not in product:
            excepts = excepts + ', 에어, air'
        if ('미니' or 'mini') not in product:
            excepts = excepts + ', 미니, mini'
        if ('플러스' or 'plus') not in product:
            excepts = excepts + ', 플러스, plus'
    
    return excepts

#상세 검색
def search_detail(excepts, adder):
    driver.switch_to.frame('cafe_main')
    
    #li[2] = 1일 / li[3] = 1주일 / li[4] = 1개월 / li[5] = 6개월 / li[6] = 1년
    driver.find_element(By.CSS_SELECTOR,'#currentSearchDateTop').click()
    time.sleep(1)
    driver.find_element(By.XPATH,'//*[@id="select_list"]/li[5]').click()
    time.sleep(1)
    driver.find_element(By.CSS_SELECTOR,'#currentSearchMenuTop').click()
    time.sleep(1)
    driver.find_element(By.XPATH, '//*[@id="divSearchMenuTop"]/ul/li[23]').click()
    time.sleep(1)

    #상세 설정
    driver.find_element(By.CSS_SELECTOR,'#detailSearchBtn').click()
    time.sleep(1)
    driver.find_element(By.XPATH,'//*[@id="srch_detail"]/div[2]/input').send_keys(excepts)
    driver.find_element(By.XPATH,'//*[@id="srch_detail"]/div[3]/input').send_keys(adder)
    driver.find_element(By.CSS_SELECTOR,'.btn-search-green').click()
    time.sleep(1)

    #팬매완료도 보기
    driver.find_element(By.XPATH,'//*[@id="searchOptionSelectDiv"]/a').click()
    time.sleep(1)
    driver.find_element(By.XPATH,'//*[@id="searchOptionSelectDiv"]/ul/li[1]/a').click()
    time.sleep(1)
    
    driver.find_element(By.XPATH, '//*[@id="listSizeSelectDiv"]/a').click()
    time.sleep(1)
    driver.find_element(By.XPATH,'//*[@id="listSizeSelectDiv"]/ul/li[7]/a').click()
    time.sleep(1)

#전체 페이지 확인
def chech_total_page():
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

    return page + num

#처음으로
def go_back(total_next_page):
    for i in reversed(range(total_next_page)):
        driver.find_element(By.XPATH,'.//*[@id="main-area"]/div[7]/a[1]').click()
    driver.find_element(By.XPATH,'.//*[@id="main-area"]/div[7]/a[1]').click()

#전체 크롤링
def Crawling_all(pid):
    total_page = chech_total_page()
    total_next_page = total_page // 10
    last_page = total_page - total_next_page * 10
    
    go_back(total_next_page)
    cur_page = 1
    
    print('--------------     Total Page : {}'.format(total_page), '    --------------')
    
    if total_next_page == 0:
        for page in range(total_page):
            print('--------------    Current Page : {}'.format(cur_page), '   --------------')
            do_Crawling(0, cur_page, pid)
            cur_page += 1
        
    else:
        for n in range(total_next_page+1):
            if n == 0:
                for page in range(10):
                    print('--------------    Current Page : {}'.format(cur_page), '   --------------')
                    do_Crawling(0, cur_page, pid)
                    cur_page += 1
            
            elif n > 0 and n != total_next_page:
                for page in range(10):
                    print('--------------    Current Page : {}'.format(cur_page), '   --------------')
                    do_Crawling(1, cur_page, pid)
                    cur_page += 1
            
            elif n == total_next_page:
                for page in range(last_page):
                    print('--------------    Current Page : {}'.format(cur_page), '   --------------')
                    do_Crawling(1, cur_page, pid)
                    cur_page += 1
    
    if cur_page > total_page:
        print('Crawling succeed!')


#한 페이지 크롤링
def do_Crawling(num, page, pid):
    with_before = 0
    
    #게시글 들어가기
    for i in range(len(driver.find_elements(By.CSS_SELECTOR, '.article'))):
        
        articles = driver.find_elements(By.CSS_SELECTOR, 'a.article')[i]
        articles.click()
        time.sleep(1)
        
        #정보 추출
        try:
            product_date = driver.find_element(By.CSS_SELECTOR, '.date').text
            product_date_str = product_date.split('.')
            del product_date_str[-1]
            product_date = '.'.join(product_date_str)
        except:
            product_date = ''
            
        try:
            product_price_str = driver.find_element(By.CSS_SELECTOR, '.cost').text
            product_price_str = product_price_str[:-1]
            product_price_str = product_price_str.replace(',', '')
            product_price = int(product_price_str)
        except:
            product_price = ''
        
        prod_price_total.append([product_date, product_price, pid])
        
        #뒤로 가기
        driver.back()
        driver.switch_to.frame('cafe_main')
    
    #다음 페이지로
    pages = '//*[@id="main-area"]/div[7]/a[{}]'.format(page+num+1)
    if isExistXpath(pages):
        WebDriverWait(driver,3).until(EC.presence_of_element_located((By.XPATH,pages))).click()

#상품명 불러오기
get_product_list()


options = Options()
#options.add_argument('headless') # headless는 화면이나 페이지 이동을 표시하지 않고 동작하는 모드

user_id = '네이버아이디'
user_pw = '네이버비번'
tablet_price_total = []

pid = 150

wb = Workbook()
prod_price_total = wb.active
prod_price_total.append(['time','price', 'product_id'])

#크롤링 시작
for idx in range(len(teblit_name)):
    driver = webdriver.Chrome(options=options)
    driver.implicitly_wait(5)
    driver.set_window_size(1920,1280)
    
    url = 'https://cafe.naver.com/joonggonara.cafe'
    driver.get(url)
    
    driver.find_element(By.CSS_SELECTOR, '#gnb_login_button').click()
    time.sleep(1)
    
    na_id = driver.find_element(By.CSS_SELECTOR, '#id')
    na_id.click()
    pyperclip.copy(user_id)
    na_id.send_keys(Keys.CONTROL, 'v')
    time.sleep(1)
    
    na_pw = driver.find_element(By.CSS_SELECTOR, '#pw')
    na_pw.click()
    pyperclip.copy(user_pw)
    na_pw.send_keys(Keys.CONTROL, 'v')
    time.sleep(1)
    
    driver.find_element(By.XPATH, '//*[@id="log.login"]').click()
    
    
    #시세를 찾을 검색어 설정/검색
    name = teblit_name[idx]
    mem = teblit_mem[idx]
    product = name + ' ' + mem
    excepts =  set_except(product) # 1개라도 포함되면 안됨
    adder = add_word[idx]
    search_name(product)
    #print(product + ', ' + mem + ', ' + excepts)
    
    #엑셀

    
    print('----- {} --- Product Name : {} || {}'.format(idx, product, mem), '------')
    
    #상세 검색
    search_detail(excepts, adder)
    
    if(isExistXpath('//*[@id="main-area"]/div[7]/a')):
        #크롤링
        Crawling_all(pid)
    
    #크롤링 종료
    driver.quit()
    pid += 1


print('----- Crawling finish! ------')
wb.save(f'./file/joonggonara_crwling_tablet_price.xlsx')
