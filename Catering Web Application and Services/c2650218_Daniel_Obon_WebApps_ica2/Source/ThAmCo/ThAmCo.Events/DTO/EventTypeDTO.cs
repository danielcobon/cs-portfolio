using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.DTO
{
    public class EventTypeDTO
    {
        [MinLength(3), MaxLength(3)]
        public string Id { get; set; }

        [Required]
        public string Title { get; set; }
    }
}
