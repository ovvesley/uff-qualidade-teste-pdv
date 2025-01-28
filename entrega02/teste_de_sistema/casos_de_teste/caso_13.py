from helpers.usuario import MENU, login

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
from selenium.common.exceptions import TimeoutException

driver = webdriver.Chrome()


def verificador_valorDespesas ():
    time.sleep(5)

    total_pagar = driver.find_element(By.XPATH, '/html/body/section[3]/div/div/div/div[3]/div/div/p[2]')
    total_pagar = total_pagar.text

    total_pagar = float(total_pagar.replace('R$', '').strip().replace(',', '.'))


    time.sleep(5)

    botao_despesas = driver.find_element(By.XPATH, '/html/body/div[3]/div/div[6]/a/img').click()

    time.sleep(5)

    linhas = driver.find_elements(By.XPATH, '/html/body/section[2]/div/div/table/tbody/tr')
    
    soma_despesas = 0.0
    for linha in linhas:
        valor_str = linha.find_element(By.XPATH, './td[4]').text
        print(f"Texto do valor da linha: '{valor_str}'")  # Depuração para verificar os valores extraídos
        try:
            valor = float(valor_str.replace('R$', '').strip().replace(',', '.'))
            soma_despesas += valor
        except ValueError as e:
            print(f"Erro ao converter o valor '{valor_str}' para float: {e}")
            continue

    time.sleep(5)
    print(f"Soma das despesas: {soma_despesas}")
    time.sleep(5)

    assert total_pagar == soma_despesas, "valores das despesas e total a pagar não coincidem"

try:

    time.sleep(5)

    
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    verificador_valorDespesas()

finally:
    driver.quit()

