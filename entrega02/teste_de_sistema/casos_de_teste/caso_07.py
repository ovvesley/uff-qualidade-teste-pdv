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

def editar_usuario(nome):
    time.sleep(5)

    botao_pessoas = driver.find_element(By.XPATH, '/html/body/div[3]/div/div[8]/a/img').click()
    time.sleep(5)

    lapis = driver.find_element(By.XPATH, '/html/body/section[2]/div/table/tbody/tr[2]/td[6]/a').click()
    time.sleep(5)

    campo_nome = driver.find_element(By.XPATH, '//*[@id="nome"]').send_keys(nome)
    time.sleep(5)
    botao_salvar = driver.find_element(By.XPATH, '//*[@id="form_pessoa"]/input[2]').click()
    time.sleep(5)
    
    try:

        WebDriverWait(driver, 5).until(EC.alert_is_present())  
        alert = driver.switch_to.alert 
        print(f"Texto do alerta: {alert.text}") 
        alert.accept() 
        assert True, "usuario foi alterado"

    except TimeoutException:
        print("Nenhum alerta foi encontrado.")
        assert False, "Erro: usuario nao foi alterado"
    
    botao_listar = driver.find_element(By.XPATH, '//*[@id="btn-padrao"]/a/span').click()
    time.sleep(5)
    
    try:
        nome_na_tabela = WebDriverWait(driver, 10).until(
            EC.visibility_of_element_located((By.XPATH, '/html/body/section[2]/div/table/tbody/tr[2]/td[2]'))
        ).text
        assert nome_na_tabela == nome, f"Nome esperado '{nome}', mas foi encontrado '{nome_na_tabela}'"
        print("Nome atualizado com sucesso!")
    except TimeoutException:
        assert False, "O nome atualizado não foi encontrado na tabela no tempo esperado."


try:

    time.sleep(5)

    
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    editar_usuario("teste")

finally:
    driver.quit()




