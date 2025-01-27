#Adicionar forma de pagamento

from helpers.usuario import MENU, login

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

driver = webdriver.Chrome()


def add_pagamento(descricao, formaPagamento):
     
    time.sleep(5)

    campo_pagamentos = driver.find_element(By.XPATH, '/html/body/div[3]/div/div[11]/a/img').click()

    time.sleep(5)

    campo_NovoPagamento = driver.find_element(By.XPATH, '//*[@id="btn-padrao"]/a').click()

    time.sleep(5)

    campo_descricao = driver.find_element(By.XPATH, '//*[@id="descricao"]').send_keys(descricao)

    time.sleep(5)

    campo_formaPagamento = driver.find_element(By.XPATH, '//*[@id="formaPagamento"]').send_keys(formaPagamento)

    time.sleep(5)

    campo_salvar = driver.find_element(By.XPATH, '//*[@id="form_categoria"]/input[2]').click()

try:
    
    time.sleep(5)

    
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    add_pagamento(
        descricao="boleto",
        formaPagamento=10
    )

    add_pagamento(
        descricao="fiado",
        formaPagamento=0
    )

    time.sleep(5)

finally:
    driver.quit()
    


