
#editar fornecedor
#precisa fazer o caso 08 antes

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


def edita_fornecedor(nome):
    time.sleep(5)

    campo_fornecedor = driver.find_element(By.XPATH, '/html/body/div[3]/div/div[9]/a/img').click()

    time.sleep(5)

    botao_lapis = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/table/tbody/tr[3]/td[7]/a/span').click()

    time.sleep(5)

    campo_nome = driver.find_element(By.XPATH, '//*[@id="nomefantasia"]').send_keys(nome)

    time.sleep(5)

    campo_salvar = driver.find_element(By.XPATH, '//*[@id="form_fornecedor"]/input').click()

    time.sleep(5)

    campo_listar = driver.find_element(By.XPATH, '//*[@id="btn-padrao"]/a/span').click()

    try:
        WebDriverWait(driver, 10).until(
            EC.visibility_of_any_elements_located((By.XPATH, f'/html/body/section[2]/div/div/table/tbody/tr/td[contains(text(), "{nome}")]'))
        )
        assert driver.find_element(By.XPATH, f'/html/body/section[2]/div/div/table/tbody/tr/td[contains(text(), "{nome}")]')
        print(f"Fornecedor com Nome Fantasia '{nome}' encontrado na tabela com sucesso.")

    except TimeoutException:
        print(f"Fornecedor com Nome Fantasia '{nome}' não apareceu na tabela a tempo.")
        raise AssertionError(f"Fornecedor com Nome Fantasia '{nome}' não foi encontrado na tabela.")


try:

    time.sleep(5)

    
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    edita_fornecedor(
        nome= "clara"
    )

finally:
    driver.quit()