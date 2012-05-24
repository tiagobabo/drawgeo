class CreateDraws < ActiveRecord::Migration
  def change
    create_table :draws do |t|
      t.string :id_creator
      t.float :latitude
      t.float :longitude
      t.text :draw
      t.boolean :challenge
      t.text :description
      t.string :password

      t.timestamps
    end
  end
end
