# Abrir um pedido e confirmar se ele foi salvo

from helpers.usuario import MENU, login, hover_lateral_menu

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select

import time

driver = webdriver.Chrome()

def encontrar_valor_pedidos():
    valor_pedidos = driver.find_element(By.XPATH, "/html/body/section[3]/div/div/div/div[1]/div/div/p[2]")
    valor_pedidos = int(valor_pedidos.text)
    return valor_pedidos

try:
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    valor_antigo = encontrar_valor_pedidos()

    # abrir pedidos
    hover_lateral_menu(driver, "pedidos")

    # criar pedido
    novo_pedido_botao = driver.find_element(By.XPATH, """//*[@id="grupoPainelCaixa"]/div[3]/a""").click()

    dropdown = driver.find_element(By.ID, "cliente")
    drop = Select(dropdown).select_by_visible_text("João Rafael Mendes Nogueira")

    observacao_txt = driver.find_element(By.ID, "observacao").send_keys("Novo Pedido")

    salvar_botao = driver.find_element(By.XPATH, """//*[@id="btn-salva"]""").click()

    # abrir pedidos
    hover_lateral_menu(driver, "inicio")

    valor_atual = encontrar_valor_pedidos()

    assert valor_atual == valor_antigo + 1, f"Erro: valor esperado {valor_antigo + 1}, mas foi {valor_atual}."

    print("Pedido criado com sucesso e o valor de pedidos foi incrementado corretamente!")

    time.sleep(5)
finally:
    # Fechar o navegador
    driver.quit()