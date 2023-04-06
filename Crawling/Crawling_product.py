from selenium import webdriver
#import chromedriver_autoinstaller
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import time
import pandas as pd

def get_prod_items(pro_items):
    prod_data = []

    for prod_item in prod_items:
        try:
            product_name = prod_item.select('p.prod_name > a')[0].text.strip()
            product_name_list = product_name.split(',')
            del product_name_list[1]
            product_name = ' '.join(product_name_list)
            product_name_list = product_name.split()
            
            company_name = product_name_list[0]
            del product_name_list[0]
            product_name = ' '.join(product_name_list)
        except:
            product_name = ""

        try:
            model_name = prod_item.select('span.cm_mark')[0].text.strip()
            model_name_list = model_name.split(':')
            del model_name_list[0]
            model_name = ''.join(model_name_list)
        except:
            model_name = ""


        mylist = ["phone", product_name, model_name, company_name]

        prod_data.append(mylist)

    return(prod_data)

# 다나와 사이트 검색
 
options = Options()
options.add_argument('headless'); # headless는 화면이나 페이지 이동을 표시하지 않고 동작하는 모드

#chromedriver_autoinsttraller.install() #크롬 드라이버 자동 설치
driver = webdriver.Chrome(options=options)
driver.implicitly_wait(5)
driver.set_window_size(1920,1280)

url = 'https://prod.danawa.com/list/?cate=12215709'
driver.get(url)
curPage = 1
totalPage = 6

prod_data_total = []

while curPage <= totalPage:
    soup = BeautifulSoup(driver.page_source, 'html.parser')
    prod_items = soup.select('li.prod_item.prod_layer')
    print('----- Current Page : {}'.format(curPage), '------')

    prod_item_list = get_prod_items(prod_items)
    prod_data_total.append(prod_item_list)

    curPage += 1

    if curPage > totalPage:
        print('Crawling succeed!')
        break

    cur_css = 'div.number_wrap > a:nth-child({})'.format(curPage)
    WebDriverWait(driver,3).until(EC.presence_of_element_located((By.CSS_SELECTOR,cur_css))).click()

    del soup

    time.sleep(3)

driver.close()

total = []
for temp in prod_data_total:
    total += temp
prod_data_total = total

data = pd.DataFrame(prod_data_total)
data.columns = ['카테고리', '상품명', '모델명', '제조사명']

data.to_excel('./file/danawa_crawling_result_class.xlsx', index =False)