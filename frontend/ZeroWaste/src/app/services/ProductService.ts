import { Injectable } from "@angular/core";
import { Product } from "../pages/products/product";
import { API_URL } from "../utils/constants";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  public async getAllProducts(daysToExpire: number | null = null): Promise<Product[]> {
    const url = `${API_URL}/products${daysToExpire ? `?daysToExpire=${daysToExpire}` : ''}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { products } = await response.json();
    return products ?? [];
  }

  public async getProductById(id: number): Promise<Product> {
    const response = await fetch(API_URL + '/products/' + id, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { product } = await response.json();
    return product ?? {};
  }

  public async deleteProduct(id: number): Promise<void> {
    const response = await fetch(API_URL + '/products/' + id, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      const body = await response.json();

      throw new Error('Error deleting product', {
        cause: body,
      });
    }
  }

  public async setDonatedStatusProduct(id: number): Promise<void> {
    const response = await fetch(API_URL + '/products/donate/' + id, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      const body = await response.json();

      throw new Error('Error setting new status', {
        cause: body,
      });
    }
  }

  public async setDiscardedStatusProduct(id: number): Promise<void> {
    const response = await fetch(API_URL + '/products/discard/' + id, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      const body = await response.json();

      throw new Error('Error setting new status', {
        cause: body,
      });
    }
  }

  public async getExpiringProducts(): Promise<number> {
    const url = `${API_URL}/products/expiring`;

    // Efetua a requisição para buscar os produtos a vencer
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    // Retorna 0 caso a requisição falhe
    if (!response.ok) {
      console.error('Erro ao buscar produtos a vencer:', response.statusText);
      return 0;
    }

    // Executa a conversão de String para inteiro
    const data = await response.json();
    return parseInt(data.expiring_products, 10) || 0;
  }

}
