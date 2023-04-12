from selenium import webdriver
#import chromedriver_autoinstaller
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import time
import pandas as pd

def get_prod_items(pro_items):    #크롤링 할 컨텐츠
    prod_data = []
    
    for prod_item in prod_items:
        try:  #상품명 크롤링
            product_name = prod_item.select('p.prod_name > a')[0].text.strip()
            product_name = product_name.replace(", 자급제", "")
            product_name_list = product_name.split()
            del product_name_list[0]
            product_name = ' '.join(product_name_list)
        except:
            product_name = ""

        try:  #상품 크롤링
            product_list = prod_item.select('div.spec_list')[0].text.strip()
            product_list = product_list.replace('보안/기능', '보안 및 기능')
            product_detail_list = product_list.split('/')

            release_os = ''
            screen_info = ''
            system = ''
            ram = ''
            mem = ''
            connect = ''
            camera = ''
            sound = ''
            function = ''
            battery = ''
            size = ''
            bench_mark = ''
            
            for i in range(len(product_detail_list)):
                if '출시OS' in product_detail_list[i]:
                    idx_os = i
                elif '화면정보' in product_detail_list[i]:
                    idx_scr = i
                elif '시스템' in product_detail_list[i]:
                    idx_sys = i
                elif '통신' in product_detail_list[i]:
                    idx_conn = i
                elif '카메라' in product_detail_list[i]:
                    idx_cam = i
                elif '사운드' in product_detail_list[i]:
                    idx_sound = i
                elif '보안 및 기능' in product_detail_list[i]:
                    idx_func = i
                elif '배터리' in product_detail_list[i]:
                    idx_bat = i
                elif '규격' in product_detail_list[i]:
                    idx_size = i
                elif '벤치마크' in product_detail_list[i]:
                    idx_bench = i
        
            release_os_list = product_detail_list[idx_os]
            release_os = ''.join(release_os_list)
            release_os = release_os.replace(' 출시OS: ','').replace(' ', '')
        
            screen_info_list = product_detail_list[idx_scr:idx_sys]
            screen_info = ''.join(screen_info_list)
            screen_info = screen_info.replace(' 화면정보 ','').replace('  ', '/ ')
        
            system_list = product_detail_list[idx_sys:idx_conn]
            ram_list = []
            mem_list = []
            
            for j in system_list:
                if '램' in j:
                    ram_list += j
                    system_list.remove(j)
            for k in system_list:
                if '내장' in k:
                    mem_list += k
                    system_list.remove(k)
                    
            system = ''.join(system_list)
            ram = ''.join(ram_list)
            mem = ''.join(mem_list)
            system = system.replace(' 시스템 ','').replace('  ', '/ ')
            ram = ram.replace('램:', '')
            mem = mem.replace('내장:', '')
            
            connect_list = product_detail_list[idx_conn:idx_cam]
            connect = ''.join(connect_list)
            connect = connect.replace(' 통신 ','').replace('  ', '/ ')
        
            camera_list = product_detail_list[idx_cam:idx_sound]
            camera = ''.join(camera_list)
            camera = camera.replace(' 카메라 ','').replace('  ', '/ ')
        
            sound_list = product_detail_list[idx_sound:idx_func]
            sound = ''.join(sound_list)
            sound = sound.replace(' 사운드 ','').replace('  ', '/ ')
        
            function_list = product_detail_list[idx_func:idx_bat]
            function = ''.join(function_list)
            function = function.replace(' 보안 및 기능 ','').replace('  ', '/ ')
        
            battery_list = product_detail_list[idx_bat:idx_size]
            battery = ''.join(battery_list)
            battery = battery.replace(' 배터리 ','').replace('  ', '/ ')

            if idx_bench:
                size_list = product_detail_list[idx_size:idx_bench]
                size = ''.join(size_list)
                size = size.replace(' 규격 ','').replace('  ', '/ ')
                bench_mark_list = product_detail_list[idx_bench:]
                bench_mark = ''.join(bench_mark_list)
                bench_mark = bench_mark.replace(' 벤치마크 ','').replace('  ', '/ ')
            else:
                size_list = product_detail_list[idx_size:]
                size = ''.join(size_list)
                size = size.replace(' 규격 ','').replace('  ', '/ ')
                bench_mark = ''

        except:
            if not release_os:
                release_os = ''
            if not screen_info:
                screen_info = ''
            if not system:
                system = ''
            if not ram:
                ram = ''
            if not mem:
                mem = ''
            if not connect:
                connect = ''
            if not camera:
                camera = ''
            if not sound:
                sound = ''
            if not function:
                function = ''
            if not battery:
                battery = ''
            if not size:
                size = ''
            if not bench_mark:
                bench_mark = ''

        try:
            img_link = 'http:' + prod_item.select_one('div.thumb_image > a > img').get('data-original')
        except:
            img_link = 'http:' + prod_item.select_one('div.thumb_image > a > img').get('src')

        mylist = [product_name, release_os, screen_info, system, ram, mem, connect, camera, sound, function, battery, size, bench_mark, img_link]
        
        if mylist[0]:
            prod_data.append(mylist)
    print(prod_data[0], prod_data[1])
    return(prod_data)

# 다나와 사이트 검색
 
options = Options()
options.add_argument('headless'); # headless는 화면이나 페이지 이동을 표시하지 않고 동작하는 모드

#chromedriver_autoinsttraller.install()
driver = webdriver.Chrome(options=options)
driver.implicitly_wait(5)
driver.set_window_size(1920,1280)

url = 'https://prod.danawa.com/list/?cate=12215709'  #핸드폰 자급제
driver.get(url)
curPage = 1  #시작 페이지
totalPage = 6  #총 페이지

prod_detail_total = []

while curPage <= totalPage:
    soup = BeautifulSoup(driver.page_source, 'html.parser')
    prod_items = soup.select('li.prod_item.prod_layer')
    print('----- Current Page : {}'.format(curPage), '------')

    prod_item_list = get_prod_items(prod_items)
    prod_detail_total.append(prod_item_list)

    curPage += 1

    if curPage > totalPage:
        print('Crawling succeed!')
        break

    cur_css = 'div.number_wrap > a:nth-child({})'.format(curPage)
    WebDriverWait(driver,3).until(EC.presence_of_element_located((By.CSS_SELECTOR,cur_css))).click()    #페이지 넘기기

    del soup

    time.sleep(3)

driver.close()

total = []
for temp in prod_detail_total:
    total += temp
prod_detail_total = total

data = pd.DataFrame(prod_detail_total)

data.columns = ['상품명', '출시OS', '화면정보', '시스템', '램', '내장메모리', '통신', '카메라', '사운드', '보안/기능', '배터리', '규격', '벤치마크', '이미지']

data.to_excel('./file/danawa_crawling_product_detail_result_class.xlsx', index =False)
