/*============================================
 * Cart
 *============================================
 * A Model for the shopping cart.
 *============================================*/

package edu.fabflix.Model;

import java.util.HashMap;
import java.util.Set;

public class Cart
{
  private HashMap<Integer, Integer> _cart;

  public Cart()
  {
    _cart = new HashMap<Integer, Integer>();
  }

  public boolean contains(int movieID)
  {
    return _cart.containsKey(movieID);
  }

  /**
   * Get the quantity of the item.
   *
   * @param int itemID The id of the item.
   * @return int The quantity of the item in the cart.
   */
  public int get(int itemID)
  {
    Integer id = _cart.get(itemID);
    return (id == null ? 0 : id);
  }

  /**
   * Gets all the item ids in the cart.
   *
   * @return Set<Integer> All the item ids in the cart. 
   */
  public Set<Integer> all()
  {
    return _cart.keySet();
  }

  /**
   * Adds an entry in the cart.
   *
   * @param int itemID The id of the item to the added to the shopping cart.
   * @param int quantity The amount of that item to add.
   */
  public void add(int itemID, int quantity)
  {
    if (_cart.containsKey(itemID) && quantity == 0)
    {
      _cart.remove(itemID);
      return;
    }
    else if (quantity == 0) return;
    _cart.put(itemID, quantity);
  }

  /**
   * Remove a item from the cart.
   *
   * @param int itemID The id of the item to remove.
   */
  public void remove(int itemID) { add(itemID, 0); }

  /**
   * Updates an entry in the cart.
   *
   * @param int itemID The item's ID to the updated to the shopping cart.
   * @param int quantity The amount of that item tto change to.
   */
  public void update(int itemID, int quantity) { add(itemID, quantity); }

  /**
   * Remove all entries from the shopping cart.
   */
  public void clear() { _cart.clear(); }
}