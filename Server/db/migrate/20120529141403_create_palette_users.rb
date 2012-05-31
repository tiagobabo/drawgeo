class CreatePaletteUsers < ActiveRecord::Migration
  def change
    create_table :palette_users do |t|
      t.integer :id_user
      t.integer :id_palette

      t.timestamps
    end
  end
end
