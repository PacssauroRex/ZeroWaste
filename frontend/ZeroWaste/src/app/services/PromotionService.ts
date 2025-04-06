import { Injectable } from "@angular/core";
import { Promotion } from "../pages/promotions/promotion";
import { API_URL } from "../utils/constants";

@Injectable({
  providedIn: 'root'
})

export class PromotionService {

  public async getPromotionById(id: number): Promise<Promotion> {

    const response = await fetch(API_URL + '/promotions/' + id, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { promotion } = await response.json();

    return promotion ?? {};

  }

  public async getAllPromotions(): Promise<Promotion[]> {

    const response = await fetch(API_URL + '/promotions/', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { promotions } = await response.json();
    return promotions ?? {};
  }

  public async getPromotionByProductName(Name: String): Promise<Promotion[]> {
    const url = `${API_URL}/promotions/productFilter/${Name}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { promotions } = await response.json();
    return promotions ?? [];
  }

  public async getPromotionByPercentage(percentage: number | null = null): Promise<Promotion[]> {
    const url = `${API_URL}/promotions/percentageFilter/${percentage}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { promotions } = await response.json();
    return promotions ?? [];
  }


  public async deletePromotion(id: number): Promise<void> {
    const response = await fetch(API_URL + '/promotions/' + id, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      const body = await response.json();

      throw new Error('Error deleting promotion', {
        cause: body,
      });
    }
  }

  public async getActivePromotions(): Promise<Promotion[]> {

    // Efetua a requisição para obter as promoções ativas
    const response = await fetch(API_URL + '/promotions/active', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Accept': 'application/json',
      }
    });

    // Em caso de erro, lança uma exceção
    if (!response.ok) {
      const body = await response.json();

      throw new Error('Error getting active promotions', {
        cause: body,
      });
    }

    // Retorna as promoções ativas
    const { promotions } = await response.json();
    return promotions ?? [];

  }
}


