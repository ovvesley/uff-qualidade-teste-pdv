# Criar Máquina cartão e Título 
#  a criação da máquina cartão e do título são necessária para a execução de outras funcionalidades no sistema PDV

from helpers.usuario import MENU, login, hover_lateral_menu

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

import time

driver = webdriver.Chrome()

def criar_maquina(descricao, taxa_debito, taxa_credito, taxa_antecipacao, qtd_dias_debito, qtd_dias_credito, banco):
    hover_lateral_menu(driver, "maquina")

    driver.find_element(By.XPATH, """//*[@id="btn-padrao"]/a""").click()

    driver.find_element(By.ID, "descricao").send_keys(descricao)

    driver.find_element(By.ID, "taxa_deb").send_keys(taxa_debito)
    driver.find_element(By.ID, "taxa_cred").send_keys(taxa_credito)
    driver.find_element(By.ID, "taxa_antec").send_keys(taxa_antecipacao)

    driver.find_element(By.ID, "qtd_dias_deb").send_keys(qtd_dias_debito)
    driver.find_element(By.ID, "qtd_dias_cred").send_keys(qtd_dias_credito)

    dropdown = driver.find_element(By.ID, "banco")
    drop = Select(dropdown).select_by_visible_text(banco)

    driver.find_element(By.XPATH, """//*[@id="form_maquinacartao"]/input[2]""").click()


def criar_titulo(descricao, tipo, maquina):
    hover_lateral_menu(driver, "titulos")
    
    driver.find_element(By.XPATH, """//*[@id="btn-padrao"]/a""").click()

    driver.find_element(By.ID, "descricao").send_keys(descricao)

    dropdown = driver.find_element(By.ID, "titulo_tipo")
    drop = Select(dropdown).select_by_visible_text(tipo)

    dropdown_maquina = driver.find_element(By.XPATH, """/html/body/section[1]/div/div/form/div[6]/div/select""")
    drop_maq = Select(dropdown_maquina).select_by_visible_text(maquina)   

    driver.find_element(By.XPATH, """/html/body/section[1]/div/div/form/input[2]""").click() 

try:
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    criar_maquina(
        descricao = "Maquina Teste",
        taxa_debito = 10, 
        taxa_credito = 20, 
        taxa_antecipacao = 30, 
        qtd_dias_debito = 1, 
        qtd_dias_credito = 2, 
        banco = "Banco 1"
    )

    driver.find_element(By.XPATH, """//*[@id="btn-padrao"]/a/span""").click() 

    maquina_elemento = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "//td[contains(text(), 'Maquina Teste')]"))
    )
    assert maquina_elemento is not None, "A máquina 'Maquina Teste' não foi encontrada na tabela."
    
    criar_titulo(
        descricao = "Titulo Teste",
        tipo = "Cartão Debito",
        maquina = "Maquina Teste"
    )

    assert "Titulo Teste" in driver.page_source, "O título não foi encontrado na lista após a criação."

    time.sleep(5)
finally:
    # Fechar o navegador
    driver.quit()