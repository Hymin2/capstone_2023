import pandas as pd

def remove_outliers(group):
    Q1 = group['price'].quantile(0.35)
    Q3 = group['price'].quantile(0.75)
    IQR = Q3 - Q1
    
    lower_bound = Q1 - 1.5 * IQR
    upper_bound = Q3 + 1.5 * IQR
    print(IQR, lower_bound, upper_bound)
    
    return group[(group['price'] >= lower_bound) & (group['price'] <= upper_bound)]

product_dp = pd.read_excel('./file/joonggonara_crwling_product_price.xlsx')
product = product_dp.groupby('product_id')

result_df = product_dp.groupby('product_id').apply(remove_outliers).reset_index(drop=True)

result_df.to_excel('./file/product_price.xlsx', index =False)
