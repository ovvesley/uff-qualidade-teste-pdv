# Criar 2 bancos pelo caixa cofre

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

def criar_banco(descricao, agencia, conta, valor_abertura):

    elemento_hover = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "/html/body/div[3]/div/div[3]/a/img"))
    )

    actions = ActionChains(driver)
    actions.move_to_element(elemento_hover).perform()

    elemento_clicar = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.XPATH, "/html/body/div[3]/div/div[3]/a"))
    )
    elemento_clicar.click()

    abrir_novo_botao = driver.find_element(By.XPATH, """//*[@id="btn-padrao"]/a""").click()

    observacao_txt = driver.find_element(By.ID, "descricao").send_keys(descricao)

    dropdown = driver.find_element(By.ID, "caixatipo")
    drop = Select(dropdown).select_by_visible_text("BANCO")
    
    agencia_txt = driver.find_element(By.ID, "agencia").send_keys(agencia)

    conta_txt = driver.find_element(By.ID, "conta").send_keys(conta)

    valor_abertura_txt = driver.find_element(By.ID, "valorAbertura").send_keys(valor_abertura)

    abrir_novo_botao = driver.find_element(By.XPATH, """//*[@id="form_caixa"]/a""").click()

try:
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    criar_banco(
        descricao= "Banco 1",
        agencia="11111",
        conta="111111",
        valor_abertura="100000"
    )

    criar_banco(
        descricao= "Banco 2",
        agencia="22222",
        conta="222222",
        valor_abertura="200000"
    )

    time.sleep(5)
finally:
    # Fechar o navegador
    driver.quit()