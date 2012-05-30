class CreatePalettes < ActiveRecord::Migration
  def change
    create_table :palettes do |t|
      t.string :hex1
      t.string :hex2
      t.string :hex3
      t.string :hex4
      t.integer :cost

      t.timestamps
    end
  end
end
