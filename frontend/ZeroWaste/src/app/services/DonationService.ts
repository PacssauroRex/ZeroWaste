import { Injectable } from "@angular/core";
import { Donation } from "../pages/donations/donation";
import { API_URL } from "../utils/constants";

@Injectable({
  providedIn: 'root'
})

export class DonationService {

  public async getAllDonations(): Promise<Donation[]> {
    const response = await fetch(API_URL + '/donations', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { donations } = await response.json();
    return donations ?? [];
  }

  public async getDonationById(id: number | null = null): Promise<Donation> {
    const url = `${API_URL}/donations/${id}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });
    const { donation } = await response.json();
    return donation ?? [];
  }


  public async deleteDonation(id: number): Promise<void> {
    const response = await fetch(API_URL + '/donations/' + id, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      const body = await response.json();

      throw new Error('Error deleting donation point', {
        cause: body,
      });
    }
  }

}


