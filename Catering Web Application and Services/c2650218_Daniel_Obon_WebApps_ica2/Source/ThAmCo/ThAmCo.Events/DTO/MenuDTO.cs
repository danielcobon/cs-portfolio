using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.DTO
{
    public class MenuDTO
    {
        public int MenuId { get; set; }

        [Required]
        [MaxLength(50)]
        public string MenuName { get; set; } = null!;

    }
}
