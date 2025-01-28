# Editar Estoque
# erro na edição, o número de estoque não é atualizado

from helpers.usuario import MENU, login, hover_lateral_menu

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.alert import Alert

import time

driver = webdriver.Chrome()

def editar_estoque(observacao, produto, qtd_estoque):
    hover_lateral_menu(driver, "estoque")

    driver.find_element(By.XPATH, """//*[@id="btn-padrao"]/a""").click()
    time.sleep(3)

    driver.find_element(By.ID, "obs").send_keys(observacao)

    dropdown_field = driver.find_element(By.XPATH, "/html/body/section[1]/div/div/div[2]/div[1]/form/div/div[1]/div/div/button")
    dropdown_field.send_keys("1")
    dropdown_field.send_keys(Keys.RETURN)

    driver.find_element(By.XPATH, """//*[@id="addproduto"]/a""").click()

    alerta = driver.switch_to.alert
    alerta.send_keys(qtd_estoque)
    alerta.accept()

    time.sleep(5)
    driver.find_element(By.XPATH, """//*[@id="tabela-produtos"]/div[2]/a[2]""").click()

    time.sleep(5)
    # alerta2 = driver.switch_to.alert
    alerta2 = WebDriverWait(driver, 10).until(EC.alert_is_present())
    alerta2.accept()

    time.sleep(5)
    alerta3 = driver.switch_to.alert
    alerta3.accept()


def confirmar_edicao_estoque():
    hover_lateral_menu(driver, "produtos") 

    dropdown_field = driver.find_element(By.XPATH, """//*[@id="descricao"]""")
    dropdown_field.send_keys("Picolé")
    dropdown_field.send_keys(Keys.RETURN)

    elemento = driver.find_element(By.XPATH, "/html/body/section[2]/div/div/table/tbody/tr/td[6]")

    valor_elemento = elemento.text.strip()

    assert valor_elemento == "10", f"Valor encontrado ({valor_elemento}) não é igual a 10"

try:
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    editar_estoque(
        observacao = "aumentar estoque de picolé",
        produto = "1",
        qtd_estoque = "10"
    )

    confirmar_edicao_estoque()

    # assert "Titulo Teste" in driver.page_source, "O título não foi encontrado na lista após a criação."

    time.sleep(5)
finally:
    # Fechar o navegador
    driver.quit()