using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Catering.Domain
{
    public class MenuFoodItem
    {
        [Required]
        public int MenuId { get; set; }

        [Required]
        public int FoodItemId { get; set; }

        // Navigation property to Menu (one-side of one-to-many)
        public Menu Menu { get; set; }

        // Navigation property to FoodItem (one-side of one-to-many)
        public FoodItem FoodItem { get; set; }
    }
}
