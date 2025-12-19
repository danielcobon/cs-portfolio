using Microsoft.EntityFrameworkCore;
using ThAmCo.Catering.Domain;

namespace ThAmCo.Catering.Data
{
    public class CateringDbContext : DbContext
    {
        public DbSet<FoodBooking> FoodBookings { get; set; } = null!;

        public DbSet<Menu> Menus { get; set; } = null!;

        public DbSet<MenuFoodItem> MenuFoodItems { get; set; } = null!;

        public DbSet<FoodItem> FoodItems { get; set; } = null!;

        private string DbPath { get; set; } = string.Empty;

        // Constructor to set-up the database path & name
        public CateringDbContext()
        {
            var folder = Environment.SpecialFolder.MyDocuments;
            var path = Environment.GetFolderPath(folder);
            DbPath = Path.Join(path, "ThAmCo.Catering.db");
        }

        // OnConfiguring to specify that the SQLite database engine will be used
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            base.OnConfiguring(optionsBuilder);
            optionsBuilder.UseSqlite($"Data Source={DbPath}");
        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            // Composite Key
            builder.Entity<MenuFoodItem>()
                .HasKey(mfs => new { mfs.MenuId, mfs.FoodItemId });

            builder.Entity<FoodItem>()
                .HasMany(fi => fi.MenuFoodItems)
                .WithOne(mfi => mfi.FoodItem)
                .HasForeignKey(mfi => mfi.FoodItemId)
                .OnDelete(DeleteBehavior.Restrict);

            builder.Entity<Menu>()
                .HasMany(m => m.MenuFoodItems)
                .WithOne(mfi => mfi.Menu)
                .HasForeignKey(mfi => mfi.MenuId)
                .OnDelete(DeleteBehavior.Restrict);

            builder.Entity<FoodBooking>()
                .HasOne(fb => fb.Menu)
                .WithMany(m => m.FoodBookings)
                .HasForeignKey(fb => fb.MenuId)
                .OnDelete(DeleteBehavior.Restrict);

            // Dummy Data
            builder.Entity<FoodBooking>().HasData(
                new FoodBooking { FoodBookingId = 1, ClientReferenceId = 1, NumberOfGuest = 1, MenuId = 1 }
            );

            builder.Entity<Menu>().HasData(
                new Menu { MenuId = 1, MenuName = "Fried Chicken" }
            );

            builder.Entity<MenuFoodItem>().HasData(
                new MenuFoodItem { MenuId = 1, FoodItemId = 1 }
            );

            builder.Entity<FoodItem>().HasData(
                new FoodItem { FoodItemId = 1, Description = "Wings only", UnitPrice = 20.50f }
            );
        }
    }
}
