using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Catering.Domain
{
    public class FoodItem
    {
        [Key]
        public int FoodItemId { get; set; }

        [MaxLength(50)]
        public string Description { get; set; } = null!;

        public float UnitPrice { get; set; }

        // Navigation property to MenuFoodItem (many-side of one-to-many)
        public List<MenuFoodItem> MenuFoodItems { get; set; }
    }
}
