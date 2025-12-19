using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Catering.Domain
{
    public class Menu
    {
        [Key]
        public int MenuId { get; set; }

        [Required]
        [MaxLength(50)]
        public string MenuName { get; set; } = null!;

        // Navigation property to FoodBooking (many-side of one-to-many)
        public List<FoodBooking> FoodBookings { get; set; }

        // Navigation property to MenuFoodItem (many-side of one-to-many)
        public List<MenuFoodItem> MenuFoodItems { get; set; }
    }
}
